package com.revature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.revature.repository.entity.AdminUser;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

	boolean existsByUserId(long user_id);

	@Transactional
	void deleteByUserId(long user_id);

	AdminUser findByUserId(long id);

}
