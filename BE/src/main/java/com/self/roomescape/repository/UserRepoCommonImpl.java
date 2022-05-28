package com.self.roomescape.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.self.roomescape.entity.*;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class UserRepoCommonImpl implements UserRepoCommon{
    private final JPAQueryFactory queryFactory;
    private EntityManager em;
    public UserRepoCommonImpl(EntityManager em){
        this.queryFactory= new JPAQueryFactory(em);        this.em=em;
    }

    @Override
    @Transactional
    public User findUserByEmail(User user) {
        User result = queryFactory
                .select(QUser.user)
                .from(QUser.user)
                .where(QUser.user.email.eq(user.getEmail()))
                .fetchOne();
        return result;
    }
}
