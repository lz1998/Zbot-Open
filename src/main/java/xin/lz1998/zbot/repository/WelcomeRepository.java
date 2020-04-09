package xin.lz1998.zbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xin.lz1998.zbot.entity.Welcome;

public interface WelcomeRepository extends JpaRepository<Welcome, Integer> {
    Welcome findWelcomeByGroupId(Long groupId);
}
