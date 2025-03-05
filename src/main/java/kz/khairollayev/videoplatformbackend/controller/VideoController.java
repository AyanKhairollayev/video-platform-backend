package kz.khairollayev.videoplatformbackend.controller;

import kz.khairollayev.videoplatformbackend.dto.VideoPreviewDto;
import kz.khairollayev.videoplatformbackend.dto.VideoUploadDto;
import kz.khairollayev.videoplatformbackend.service.VideoService;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<List<VideoPreviewDto>> getVideoList() {
        return videoService.getList();
    }

    @GetMapping("/watch")
    public String watchVideo(@RequestParam Long id, Model model) {
        model.addAttribute("id", id);
        return "video";
    }

    @GetMapping("/get-list")
    public String getVideoList(Model model) {
        model.addAttribute("list", videoService.getList().getBody());
        return "videoList";
    }
}
