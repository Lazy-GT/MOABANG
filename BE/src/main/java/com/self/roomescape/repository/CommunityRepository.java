package com.self.roomescape.repository;

import com.self.roomescape.entity.Community;
import com.self.roomescape.repository.mapping.CommunityMapping;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    List<Community> findTop5ByOrderByCreateDateDesc();

    List<Community> findAllByOrderByCreateDateDesc();

    @Query("select u as user,c.rid as rid,c.title as title,c.content as content,c.header as header," +
            "c.createDate as createDate,c.updateDate as updateDate, (select count(co) from Comment co where co.communityId=c.rid ) as count,(select count(r) from Report r where r.category=1 and r.targetId=c.rid )as reportCnt " +
            "from Community c left join User u on c.user.uid=u.uid " +
            "order by c.createDate desc "
    )
    List<CommunityMapping> findCustomList();

    List<Community> findAllByUser_Uid(long uid);

    List<Community> findByUserUidOrderByCreateDate(long uid);
}
