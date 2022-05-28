package com.self.roomescape.config;

import com.self.roomescape.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.self.roomescape.entity.User;
import com.self.roomescape.service.UserService;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String userPk) {
        User user = userRepository.findByEmail(userPk).get();

        return (UserDetails) user;
    }
}