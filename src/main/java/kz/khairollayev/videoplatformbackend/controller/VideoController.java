package kz.khairollayev.videoplatformbackend.controller;

import kz.khairollayev.videoplatformbackend.dto.VideoPreviewDto;
import kz.khairollayev.videoplatformbackend.dto.VideoUploadDto;
import kz.khairollayev.videoplatformbackend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(VideoUploadDto videoUploadDto) {
        return ResponseEntity.ok(videoService.uploadVideo(videoUploadDto));

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteVideo(@PathVariable Long id) {
        videoService.deleteVideo(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stream/{id}")
    public ResponseEntity<ResourceRegion> streamVideoFromDb(
            @PathVariable Long id,
            @RequestHeader HttpHeaders headers) {
        return videoService.streamVideo(id, headers);
    }

    @GetMapping()
    public ResponseEntity<List<VideoPreviewDto>> getVideoList() {
        return ResponseEntity.ok(videoService.getList());
    }

    @GetMapping("/preview/{id}")
    public ResponseEntity<byte[]> getPreviewPhoto(@PathVariable Long id) {
        byte[] image = videoService.getPreviewPhotoById(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}
