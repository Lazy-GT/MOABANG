package com.self.roomescape.repository.mapping;

import com.self.roomescape.entity.Cafe;
import com.self.roomescape.entity.Theme;

import java.util.Date;

public interface ThemeListMapping {
    int getTid();

    int getCid();

    String getTname();

    String getImg();

    String getDescription();

    String getRplayer();

    String getTime();

    String getGenre();

    String getUrl();

    String getType();

    String getCname();

    String getCurl();

    String getIsland();

    String getSi();

    int getDifficulty();

    float getGrade();

    String getActivity();

    Long getCount();

    Date getPlayDate();

    int getIsSuccess();

    int getPlayer();

    String getLon();

    String getLat();
}
