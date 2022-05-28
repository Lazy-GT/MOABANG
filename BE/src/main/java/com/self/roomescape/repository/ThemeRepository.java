package com.self.roomescape.repository;

import com.self.roomescape.entity.Theme;
import com.self.roomescape.repository.mapping.MyPageTidMapping;
import com.self.roomescape.repository.mapping.ThemeListMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import response.ThemeDetailDTO;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> { // ,c.url as curl,c.cname as cname
    List<Theme> findByCafeCid(int cid);

    //    List<Theme> findAllBy();
    Theme findThemesByTid(int tid);
//    @Query(value = "select t.tid as tid,t.cid as cid, t.tname as tname , t.img as img ,t.description as description,t.difficulty as difficulty,t.rplayer as rplayer, t.time as time, t.genre as genre, t.type as type,t.grade as grade,t.activity as activity,c.cname as cname,c.url as curl,c.island as island, c.si as si from  Theme t left join Cafe c on c.cid=t.cid")
//    List<ThemeListMapping> findThemeAndCafe();

    @Query("select " +
            "(select count(u) from UserLike u where u.theme.tid = t.tid ) as count,\n" +
            "c.island as island,c.cname as cname,c.si as si,c.cid as cid,c.url as url,t.img as img,t.tid as tid,t.tname as tname,t.description as description,t.rplayer as rplayer,t.time as time,t.genre as genre,t.type as type,t.difficulty as difficulty,t.grade as grade,t.activity as activity " +
            "from Theme t join t.cafe c " +
            "on t.cafe.cid=c.cid " +
            "where t.tid in (select u.theme.tid from UserLike u where u.user.uid = :uid)")
    List<ThemeListMapping> findThemeFetch(@Param("uid") long uid);

    @Query("select " +
            "c.cname as cname,r.playDate as playDate,t.img as img,t.tid as tid,t.tname as tname,r.rating as grade,r.isSuccess as isSuccess,r.player as player " +
            "from Theme t join t.cafe c " +
            "on t.cafe.cid=c.cid " +
            "join Review r " +
            "on r.tid=t.tid  " +
            "where r.userInfo.uid = :uid ")
    List<ThemeListMapping> findThemeAndReviewFetch(@Param("uid") long uid);

    @Query("select t.tid as tid from Theme t left join Review r on t.tid=r.tid where r.userInfo.uid=:uid ")
    List<MyPageTidMapping> findTidFetch(@Param("uid") long uid);

    Optional<Theme> findByTid(int tid);

    List<Theme> findAll();
}
