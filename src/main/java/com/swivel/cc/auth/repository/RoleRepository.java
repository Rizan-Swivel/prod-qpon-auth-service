package com.swivel.cc.auth.repository;

import com.swivel.cc.auth.domain.entity.Role;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    /**
     * Get RoleBy Name
     *
     * @param roleName roleName
     * @return role
     */
    Role findByName(String roleName);

    /**
     * This method return default role
     *
     * @return Role
     */
    @Query(value = "SELECT * FROM role WHERE id = 1", nativeQuery = true)
    Role findUserRole();

    @Query(value = "SELECT NAME FROM role where deletable=false", nativeQuery = true)
    List<String> findNameOfDefaultRoles();


}
