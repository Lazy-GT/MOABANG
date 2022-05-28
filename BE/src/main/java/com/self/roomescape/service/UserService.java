package com.self.roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.self.roomescape.entity.User;
import com.self.roomescape.repository.UserRepository;


@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = false)
    public void saveUser(User user) {
        userRepository.save(user);
    }
//    @Transactional(readOnly = true)
//    public User findUserByEmail(String email){
//        return userRepository.findByEmail(email);
//    }

    @Transactional(readOnly = false)
    public User findUserById(long id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(RuntimeException::new);
        return user;
    }

}
