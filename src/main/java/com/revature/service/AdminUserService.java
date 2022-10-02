package com.revature.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.repository.AdminUserRepository;
import com.revature.repository.entity.AdminUser;

@Service
public class AdminUserService {

	private AdminUserRepository adminUserRepository;

	@Autowired
	public AdminUserService(AdminUserRepository adminUserRepository) {
		this.adminUserRepository = adminUserRepository;
	}

	public AdminUserRepository getAdminUserRepository() {
		return adminUserRepository;
	}

	public void setAdminUserRepository(AdminUserRepository adminUserRepository) {
		this.adminUserRepository = adminUserRepository;
	}

	@Override
	public int hashCode() {
		return Objects.hash(adminUserRepository);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdminUserService other = (AdminUserService) obj;
		return Objects.equals(adminUserRepository, other.adminUserRepository);
	}

	@Override
	public String toString() {
		return "AdminUserService [adminUserRepository=" + adminUserRepository + "]";
	}
	
	public boolean exists(long id)
	{
		return adminUserRepository.existsById(id);
	}
	
	public boolean isAdmin(long user_id)
	{
		return adminUserRepository.existsByUserId(user_id);
	}
	
	public AdminUser save(AdminUser adminUser)
	{
		return adminUserRepository.save(adminUser);
	}
	
	public List<AdminUser> findAll()
	{
		return adminUserRepository.findAll();
	}
	
	public void deleteById(long id)
	{
		adminUserRepository.deleteById(id);
	}
	
	public void deleteByUserId(long user_id)
	{
		adminUserRepository.deleteByUserId(user_id);
	}

	public AdminUser findByUserId(long id) {
		return adminUserRepository.findByUserId(id);
	}
}
