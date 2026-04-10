package org.geli.marketplace.repository;

import org.geli.marketplace.model.ActivityLogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLogModel, Long> {
}
