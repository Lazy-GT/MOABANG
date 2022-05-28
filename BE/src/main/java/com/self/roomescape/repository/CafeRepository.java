package com.self.roomescape.repository;

import com.self.roomescape.entity.Cafe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


@Repository
public interface CafeRepository extends JpaRepository<Cafe, Long>, CafeRepoCommon {
    List<Cafe> findAll();
    Cafe findCafeByCid(int cid);


}
