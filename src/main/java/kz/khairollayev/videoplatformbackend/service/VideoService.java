package kz.khairollayev.videoplatformbackend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface VideoService {
    String uploadVideo(MultipartFile file);
    ResponseEntity<byte[]> streamVideo(Long id, String rangeHeader);
}
