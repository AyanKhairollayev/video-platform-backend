package kz.khairollayev.videoplatformbackend.controller;

import kz.khairollayev.videoplatformbackend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(videoService.uploadVideo(file));
    }

    @GetMapping(value = "/stream/{id}")
    public ResponseEntity<byte[]> streamVideo(@PathVariable Long id,
                                              @RequestHeader(value = "Range", required = false) String rangeHeader) {

        return videoService.streamVideo(id, rangeHeader);
    }
}
