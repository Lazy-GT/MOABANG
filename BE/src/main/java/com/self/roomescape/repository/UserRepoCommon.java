package com.self.roomescape.repository;

import org.springframework.stereotype.Repository;
import com.self.roomescape.entity.User;

@Repository
public interface UserRepoCommon {
    User findUserByEmail(User user);
}
