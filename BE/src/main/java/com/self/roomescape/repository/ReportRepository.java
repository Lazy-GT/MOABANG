package com.self.roomescape.repository;

import com.self.roomescape.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByUserUidAndCategoryAndTargetId(long uid, int category, int target_id);
}
