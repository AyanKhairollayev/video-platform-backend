package kz.khairollayev.videoplatformbackend.service;

import kz.khairollayev.videoplatformbackend.dto.LoginDto;
import kz.khairollayev.videoplatformbackend.dto.RegistrationDto;
import kz.khairollayev.videoplatformbackend.model.UserEntity;

public interface UserService {
    UserEntity register(RegistrationDto registrationDto);
    String login(LoginDto loginDto);
}
