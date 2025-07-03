package kz.khairollayev.videoplatformbackend.controller;

import kz.khairollayev.videoplatformbackend.dto.VideoPreviewDto;
import kz.khairollayev.videoplatformbackend.dto.VideoUploadDto;
import kz.khairollayev.videoplatformbackend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(VideoUploadDto videoUploadDto) {
        return ResponseEntity.ok(videoService.uploadVideo(videoUploadDto));

    }

    @GetMapping(value = "/stream/{id}")
    public ResponseEntity<byte[]> streamVideo(@PathVariable Long id,
                                              @RequestHeader(value = "Range", required = false) String rangeHeader) {

        return videoService.streamVideo(id, rangeHeader);
    }

    @GetMapping("/list")
    public ResponseEntity<List<VideoPreviewDto>> getVideoList(Model model) {
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
