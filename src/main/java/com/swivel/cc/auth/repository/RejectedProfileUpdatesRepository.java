package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.RejectedProfileUpdates;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RejectedProfileUpdatesRepository extends JpaRepository<RejectedProfileUpdates, String> {
}
