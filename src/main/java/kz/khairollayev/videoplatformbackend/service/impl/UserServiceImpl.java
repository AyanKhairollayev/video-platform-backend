package kz.khairollayev.videoplatformbackend.service.impl;

import kz.khairollayev.videoplatformbackend.dto.LoginDto;
import kz.khairollayev.videoplatformbackend.dto.RegistrationDto;
import kz.khairollayev.videoplatformbackend.model.UserEntity;
import kz.khairollayev.videoplatformbackend.repository.UserRepository;
import kz.khairollayev.videoplatformbackend.security.JwtUtil;
import kz.khairollayev.videoplatformbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity register(@RequestBody RegistrationDto registrationDto) {
        if(userRepository.findByUsername(registrationDto.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already in use");
        }

        UserEntity userEntity = UserEntity.builder()
                .email(registrationDto.getEmail())
                .username(registrationDto.getUsername())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .build();

        userRepository.save(userEntity);

        return userEntity;
    }

    @Override
    public String login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
        );

        return jwtUtil.generateToken(loginDto.getUsername());
    }
}
