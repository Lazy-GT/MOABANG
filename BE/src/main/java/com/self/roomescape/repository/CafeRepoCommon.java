package com.self.roomescape.repository;

import org.springframework.stereotype.Repository;
import response.ThemeDetailDTO;

import java.util.List;

@Repository
public interface CafeRepoCommon {
//    List<Tuple> getthemeList();
    List<ThemeDetailDTO> getthemeList();
    ThemeDetailDTO gettheme(int tid);
}
