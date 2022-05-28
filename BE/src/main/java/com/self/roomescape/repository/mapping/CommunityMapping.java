package com.self.roomescape.repository.mapping;

import com.self.roomescape.entity.User;

import java.time.LocalDateTime;
import java.util.Date;

public interface CommunityMapping {
    long getRid();

    User getUser();

    String getTitle();

    String getContent();

    String getHeader();

    LocalDateTime getCreateDate();

    LocalDateTime getUpdateDate();

    int getCount();

    int getReportCnt();
}
