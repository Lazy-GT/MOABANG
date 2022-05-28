package com.self.roomescape.repository;//package com.self.roomescape.repository;
//

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.self.roomescape.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.Arrays;
import java.util.List;

@Repository
@Transactional
public class UserLikeRepoCommonImpl implements UserLikeRepoCommon {
    private final JPAQueryFactory queryFactory;
    private EntityManager em;

    public UserLikeRepoCommonImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

//    @Override
//    @Transactional
//    public List<UserLike> findThemeList(long uid) {
//
//        System.out.println("Impl : " + uid);
//
////        List<UserLike> res = em.createQuery("select " +
//////                        "(select count(u) from UserLike u where u.theme.tid = t.tid ) as count,\n" +
//////                        "c.cname,c.url,c.island,c.si,t.tid,t.cafe.cid,t.tname,t.img,t.description,t.difficulty,t.rplayer,t.time,t.genre,t.type,t.grade,t.activity " +
////                        "c,t " +
////                        "from Theme t left join Cafe c " +
////                        "on t.cafe.cid=c.cid " +
////                        "where t.tid in (select u.theme.tid from UserLike u where u.user.uid = :uid)", UserLike.class)
////                .setParameter("uid", uid)
////                .getResultList();
//
//        return em.createQuery("select u " +
//                        "from UserLike u " +
//                        "where u.user.uid = :uid", UserLike.class)
//                .setParameter("uid", uid)
//                .getResultList();
//
//    }
}
