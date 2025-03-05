package kz.khairollayev.videoplatformbackend.service;

import kz.khairollayev.videoplatformbackend.dto.VideoUploadDto;
import kz.khairollayev.videoplatformbackend.dto.VideoPreviewDto;
import kz.khairollayev.videoplatformbackend.model.Video;
import kz.khairollayev.videoplatformbackend.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private final VideoRepository videoRepository;

    @Override
    public String uploadVideo(VideoUploadDto videoUploadDto) {
        try {
            Video video = new Video();
            video.setName(videoUploadDto.getName());
            video.setData(videoUploadDto.getMultipartFile().getBytes());
            video.setContentType(videoUploadDto.getMultipartFile().getContentType());
            video.setPreviewPhoto(videoUploadDto.getPreviewPhoto().getBytes());
            videoRepository.save(video);
            return "Uploaded successfully! Video ID: " + video.getId();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public ResponseEntity<List<VideoPreviewDto>> getList() {
        List<Video> videoList = videoRepository.findAll();
        List<VideoPreviewDto> videoPreviewDtoList = new ArrayList<>();

        for (Video video : videoList) {
            VideoPreviewDto videoPreviewDto = new VideoPreviewDto();
            videoPreviewDto.setId(video.getId());
            videoPreviewDto.setName(video.getName());
            videoPreviewDto.setPreviewPhoto(video.getPreviewPhoto());

            videoPreviewDtoList.add(videoPreviewDto);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return ResponseEntity.ok().body(videoPreviewDtoList);
    }

    @Override
    public ResponseEntity<byte[]> streamVideo(Long id, String rangeHeader) {
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
