package kz.khairollayev.videoplatformbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoPreviewDto {
    private Long id;
    private byte[] previewPhoto;
    private String name;
}
