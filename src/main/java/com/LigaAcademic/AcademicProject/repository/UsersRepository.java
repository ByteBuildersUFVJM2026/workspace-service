package com.LigaAcademic.AcademicProject.repository;

import com.LigaAcademic.AcademicProject.User.User;
import com.LigaAcademic.AcademicProject.User.UsersRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsersRepository extends JpaRepository<User, UUID> {

    UserDetails findByEmail(String email);

    boolean existsByRole(UsersRoles role);

    boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.role = :role WHERE u.email = :email")
    void updateRoleByEmail(@Param("email") String email, @Param("role") UsersRoles role);
}
