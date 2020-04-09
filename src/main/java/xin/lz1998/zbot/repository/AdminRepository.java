package xin.lz1998.zbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.lz1998.zbot.entity.Admin;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Admin findAdminByGroupIdAndUserId(Long groupId, Long userId);

    List<Admin> findAdminsByGroupIdAndAndIsAdmin(Long groupId, Boolean isAdmin);
}
