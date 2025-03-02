package kz.khairollayev.videoplatformbackend.controller;

import kz.khairollayev.videoplatformbackend.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
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

    @GetMapping("/watch")
    public String watchVideo(@RequestParam Long id, Model model) {
        model.addAttribute("id", id);
        return "video";
    }
}
