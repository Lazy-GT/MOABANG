package com.self.roomescape.repository.mapping;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.self.roomescape.entity.UserInfo;

import java.sql.Date;
import java.sql.Timestamp;

public interface ReviewListMapping {
    long getRid();

//    long getUid();
//
//    String getNickName();
//
//    String getEmail();
//
//    String getPimg();

    UserInfo getUserInfo();

    int getTid();

    float getRating();

    int getIsSuccess();

    int getHint();

    int getClearTime();

    int getPlayer();

    int getRecPlayer();

    String getActive();

    int getChaegamDif();

    @JsonFormat(pattern = "yyyy.MM.dd")
    Date getPlayDate();

    @JsonFormat(pattern = "yyyy.MM.dd")
    Timestamp getRegDate();

    String getContent();

    int getReportCnt();
}
