package kz.khairollayev.videoplatformbackend.service;

import kz.khairollayev.videoplatformbackend.dto.VideoUploadDto;
import kz.khairollayev.videoplatformbackend.dto.VideoPreviewDto;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface VideoService {
    String uploadVideo(VideoUploadDto videoUploadDto);
    ResponseEntity<ResourceRegion> streamVideo(Long id, HttpHeaders headers);
    List<VideoPreviewDto> getList();
    byte[] getPreviewPhotoById(Long id);
    void deleteVideo(Long id);
}
