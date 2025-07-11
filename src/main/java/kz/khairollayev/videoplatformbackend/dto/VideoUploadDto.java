package kz.khairollayev.videoplatformbackend.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class VideoUploadDto {
    private MultipartFile file;
    private String name;
    private MultipartFile previewPhoto;
}
