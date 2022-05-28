package com.self.roomescape.repository;

import com.self.roomescape.entity.Compare;
import com.self.roomescape.entity.Theme;
import com.self.roomescape.entity.User;
import com.self.roomescape.repository.mapping.ThemeListMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompareRepository extends JpaRepository<Compare, Long> {
    Optional<Compare> findByUserAndTheme(User user, Theme theme);

    @Query("select " +
            "(select count(u) from UserLike u where u.theme.tid = t.tid ) as count,\n" +
            "c.island as island,c.cname as cname,c.si as si,c.cid as cid,c.url as url,c.lat as lat, c.lon as lon,t.img as img,t.tid as tid,t.tname as tname,t.description as description,t.rplayer as rplayer,t.time as time,t.genre as genre,t.type as type,t.difficulty as difficulty,t.grade as grade,t.activity as activity " +
            "from Theme t join t.cafe c " +
            "on t.cafe.cid=c.cid " +
            "where t.tid in (select co.theme.tid from Compare co where co.user.uid = :uid)")
    List<ThemeListMapping> findThemeFetch(@Param("uid") long uid);
}
