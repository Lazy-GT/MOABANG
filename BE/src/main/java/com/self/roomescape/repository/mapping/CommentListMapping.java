package com.self.roomescape.repository.mapping;

import java.util.Date;

public interface CommentListMapping {
    long getCoid();

    String getContent();

    Date getRegDate();

    long getUid();

    long getCommunityId();

    String getUserProfile();

    String getUserName();

    String getEmail();

    int getReportCnt();
}
