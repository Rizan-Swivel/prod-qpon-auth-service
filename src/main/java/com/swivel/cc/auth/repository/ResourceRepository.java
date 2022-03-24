package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Resource Repository
 */
public interface ResourceRepository extends JpaRepository<Resource, Integer> {
}
