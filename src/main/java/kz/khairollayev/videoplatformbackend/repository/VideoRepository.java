package kz.khairollayev.videoplatformbackend.repository;

import kz.khairollayev.videoplatformbackend.model.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Long> {
}
