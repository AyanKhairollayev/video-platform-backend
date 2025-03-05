package kz.khairollayev.videoplatformbackend.service;

import kz.khairollayev.videoplatformbackend.dto.VideoUploadDto;
import kz.khairollayev.videoplatformbackend.dto.VideoPreviewDto;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface VideoService {
    String uploadVideo(VideoUploadDto videoUploadDto);
    ResponseEntity<byte[]> streamVideo(Long id, String rangeHeader);
    ResponseEntity<List<VideoPreviewDto>> getList();
}
