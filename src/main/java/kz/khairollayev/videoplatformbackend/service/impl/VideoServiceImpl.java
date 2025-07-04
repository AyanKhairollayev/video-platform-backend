package kz.khairollayev.videoplatformbackend.service.impl;

import kz.khairollayev.videoplatformbackend.dto.VideoUploadDto;
import kz.khairollayev.videoplatformbackend.dto.VideoPreviewDto;
import kz.khairollayev.videoplatformbackend.model.Video;
import kz.khairollayev.videoplatformbackend.repository.VideoRepository;
import kz.khairollayev.videoplatformbackend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {
    private static final long CHUNK_SIZE = 1024 * 1024;

    private final VideoRepository videoRepository;

    @Override
    public void deleteVideo(Long id) {
        videoRepository.deleteById(id);
    }

    @Override
    public String uploadVideo(VideoUploadDto videoUploadDto) {
        try {
            Video video = new Video();
            video.setName(videoUploadDto.getName());
            video.setData(videoUploadDto.getFile().getBytes());
            video.setContentType(videoUploadDto.getFile().getContentType());
            video.setPreviewPhoto(videoUploadDto.getPreviewPhoto().getBytes());
            videoRepository.save(video);
            return "Uploaded successfully! Video ID: " + video.getId();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @Override
    public List<VideoPreviewDto> getList() {
        List<Video> videoList = videoRepository.findAll();
        List<VideoPreviewDto> videoPreviewDtoList = new ArrayList<>();

        for (Video video : videoList) {
            VideoPreviewDto dto = new VideoPreviewDto();
            dto.setId(video.getId());
            dto.setName(video.getName());
            dto.setPreviewPhoto(("/videos/preview/" + video.getId()));
            videoPreviewDtoList.add(dto);
        }

        return videoPreviewDtoList.isEmpty() ? null : videoPreviewDtoList;
    }

    @Override
    public byte[] getPreviewPhotoById(Long id) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return video.getPreviewPhoto();
    }

    @Override
    public ResponseEntity<ResourceRegion> streamVideo(Long id, HttpHeaders headers) {
        Video video = videoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        byte[] allBytes = video.getData();
        ByteArrayResource resource = new ByteArrayResource(allBytes);
        long contentLength = allBytes.length;

        ResourceRegion region = extractRegion(headers, resource, contentLength);

        MediaType contentType = MediaTypeFactory
                .getMediaType(video.getName())
                .orElse(MediaType.APPLICATION_OCTET_STREAM);

        HttpStatus status = (headers.getRange().isEmpty())
                ? HttpStatus.OK
                : HttpStatus.PARTIAL_CONTENT;

        return ResponseEntity.status(status)
                .contentType(contentType)
                .body(region);
    }

    private ResourceRegion extractRegion(HttpHeaders headers,
                                         ByteArrayResource resource,
                                         long contentLength) {
        List<HttpRange> ranges = headers.getRange();
        if (ranges.isEmpty()) {
            long length = Math.min(CHUNK_SIZE, contentLength);
            return new ResourceRegion(resource, 0, length);
        }
        HttpRange range = ranges.get(0);
        long start = range.getRangeStart(contentLength);
        long end   = range.getRangeEnd(contentLength);
        long rangeLength = Math.min(CHUNK_SIZE, end - start + 1);
        return new ResourceRegion(resource, start, rangeLength);
    }
}
