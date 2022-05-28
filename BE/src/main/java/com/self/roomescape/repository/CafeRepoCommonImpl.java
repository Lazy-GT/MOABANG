package com.self.roomescape.repository;//package com.self.roomescape.repository;
//

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.self.roomescape.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import response.ThemeDetailDTO;
import response.ThemeDetailResDTO;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class CafeRepoCommonImpl implements CafeRepoCommon {
    private final JPAQueryFactory queryFactory;
    private EntityManager em;

    public CafeRepoCommonImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
        this.em = em;
    }

//    @Override
//    @Transactional
//    public Boolean check(User user, Theme theme){
//        UserLike result = queryFactory
//                .selectFrom(QUserLike.userLike)
//                .where(QUserLike.userLike.user.eq(user))
//                .fetchFirst();
//        if(result!=null){
//            return true;
//        }else{
//            return false;
//        }
//    }

    //    @Override
//    @Transactional
//    public List<Tuple> getthemeList(){
//        QTheme theme = QTheme.theme;
//        QCafe cafe = QCafe.cafe;
//        QUserLike userLike = QUserLike.userLike;
//
//        List<Tuple> result= queryFactory
//                .select(theme.tname, cafe.cname, cafe.url,
//                        queryFactory.select(userLike.count())
//                                .from(userLike)
//                                .where(userLike.theme.eq(theme)))
//                .from(theme,cafe)
//                .where(cafe.cid.eq(theme.cid))
//
//
//                .fetch();
//        System.out.println(result.size());
//        System.out.println(result);
//        return result;
//    }
    @Override
    @Transactional
    public List<ThemeDetailDTO> getthemeList() {
        QTheme theme = QTheme.theme;
        QCafe cafe = QCafe.cafe;
        QUserLike userLike = QUserLike.userLike;
        List<ThemeDetailDTO> result = queryFactory
                .select(Projections.constructor(ThemeDetailDTO.class,
                        theme.tid,
                        cafe.cid,
                        theme.tname,
                        theme.img,
                        theme.description,
                        theme.rplayer,
                        theme.time,
                        theme.genre,
                        theme.type,
                        theme.difficulty,
                        theme.grade,
                        theme.activity,
                        cafe.cname,
                        cafe.url,
                        cafe.island,
                        cafe.si,
                        queryFactory.select(userLike.count())
                                .from(userLike)
                                .where(userLike.theme.eq(theme))))
                .from(theme, cafe)
                .where(cafe.cid.eq(theme.cafe.cid))
                .fetch();

        return result;
    }

    @Override
    @Transactional
    public ThemeDetailDTO gettheme(int tid) {
        QTheme theme = QTheme.theme;
        QCafe cafe = QCafe.cafe;
        QUserLike userLike = QUserLike.userLike;
        ThemeDetailDTO result = queryFactory
                .select(Projections.constructor(ThemeDetailDTO.class,
                        theme.tid,
                        cafe.cid,
                        theme.tname,
                        theme.img,
                        theme.description,
                        theme.rplayer,
                        theme.time,
                        theme.genre,
                        theme.type,
                        theme.difficulty,
                        theme.grade,
                        theme.activity,
                        cafe.cname,
                        cafe.url,
                        cafe.island,
                        cafe.si,
                        queryFactory.select(userLike.count())
                                .from(userLike)
                                .where(userLike.theme.eq(theme))))
                .from(theme, cafe)
                .where(theme.tid.eq(tid).and(cafe.cid.eq(theme.cafe.cid)))
                .fetchOne();

        return result;
    }
}
