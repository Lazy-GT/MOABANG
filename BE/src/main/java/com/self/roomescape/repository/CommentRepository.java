package com.self.roomescape.repository;

import com.self.roomescape.entity.Comment;
import com.self.roomescape.repository.mapping.CommentListMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //RegDate로 order desc 해주기.
    @Query("select co.coid as coid, co.content as content, co.regDate  as regDate, co.user.uid as uid, co.communityId as communityId,co.user.pimg as userProfile, co.user.nickname  as  userName,co.user.email as email,(select count(r) from Report r where r.category=2 and r.targetId=co.coid ) as reportCnt  from Comment co where co.communityId=:community_id order by co.regDate desc ")
    List<CommentListMapping> findByCustomCommentList(@Param("community_id") long community_id);

    Optional<Comment> findByCoid(long coid);
}
