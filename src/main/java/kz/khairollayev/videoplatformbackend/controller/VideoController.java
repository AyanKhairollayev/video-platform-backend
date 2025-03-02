package kz.khairollayev.videoplatformbackend.controller;

import kz.khairollayev.videoplatformbackend.model.Video;
import kz.khairollayev.videoplatformbackend.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoRepository videoRepository;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            Video video = new Video();
            video.setName(file.getOriginalFilename());
            video.setData(file.getBytes());
            video.setContentType(file.getContentType());
            videoRepository.save(video);
            return ResponseEntity.ok("Uploaded successfully! Video ID: " + video.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading video");
        }
    }

    @GetMapping(value = "/stream/{id}")
    public ResponseEntity<byte[]> streamVideo(@PathVariable Long id,
                                              @RequestHeader(value = "Range", required = false) String rangeHeader) {
        Optional<Video> videoOptional = videoRepository.findById(id);
        if (!videoOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Video video = videoOptional.get();
        byte[] videoData = video.getData();
        long videoLength = videoData.length;

        String mimeType = (video.getContentType() != null) ? video.getContentType() : "video/mp4";

        if (rangeHeader == null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf(mimeType))
                    .body(videoData);
        }

        String[] ranges = rangeHeader.replace("bytes=", "").split("-");
        long start = Long.parseLong(ranges[0]);
        long end = (ranges.length > 1 && !ranges[1].isEmpty()) ? Long.parseLong(ranges[1]) : videoLength - 1;
        long contentLength = end - start + 1;

        byte[] partialData = Arrays.copyOfRange(videoData, (int) start, (int) end + 1);

        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + videoLength)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .contentLength(contentLength)
                .contentType(MediaType.valueOf(mimeType))
                .body(partialData);
    }
}
