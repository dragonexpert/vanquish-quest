package com.revature.repository.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "admin_users")
public class AdminUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NaturalId
	@Column(name = "user_id")
	private long userId;
	
	@Column(name = "can_manage_shop")
	private boolean canManageShop;
	
	@Column(name = "can_manage_admins")
	private boolean canManageAdmins;
	
	@Column(name = "can_delete_chat_messages")
	private boolean canDeleteChatMessages;
	
	@Column(name = "reset_password")
	private boolean canResetPassword;
	
	

	public AdminUser() {
		this.userId = 0;
		this.canManageShop = false;
		this.canManageAdmins = false;
		this.canDeleteChatMessages = false;
		this.canResetPassword = false;
	}

	public AdminUser(long id, long userId, boolean canManageShop, boolean canManageAdmins,
			boolean canDeleteChatMessages, boolean canResetPassword) {
		this.id = id;
		this.userId = userId;
		this.canManageShop = canManageShop;
		this.canManageAdmins = canManageAdmins;
		this.canDeleteChatMessages = canDeleteChatMessages;
		this.canResetPassword = canResetPassword;
	}
	
	// Check if the current user is an admin
	public boolean isAdmin()
	{
		return this.userId != 0;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public boolean isCanManageShop() {
		return canManageShop;
	}

	public void setCanManageShop(boolean canManageShop) {
		this.canManageShop = canManageShop;
	}

	public boolean isCanManageAdmins() {
		return canManageAdmins;
	}

	public void setCanManageAdmins(boolean canManageAdmins) {
		this.canManageAdmins = canManageAdmins;
	}

	public boolean isCanDeleteChatMessages() {
		return canDeleteChatMessages;
	}

	public void setCanDeleteChatMessages(boolean canDeleteChatMessages) {
		this.canDeleteChatMessages = canDeleteChatMessages;
	}

	public boolean isCanResetPassword() {
		return canResetPassword;
	}

	public void setCanResetPassword(boolean canResetPassword) {
		this.canResetPassword = canResetPassword;
	}

	@Override
	public int hashCode() {
		return Objects.hash(canDeleteChatMessages, canManageAdmins, canManageShop, canResetPassword, id, userId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdminUser other = (AdminUser) obj;
		return canDeleteChatMessages == other.canDeleteChatMessages && canManageAdmins == other.canManageAdmins
				&& canManageShop == other.canManageShop && canResetPassword == other.canResetPassword && id == other.id
				&& userId == other.userId;
	}

	@Override
	public String toString() {
		return "AdminUser [id=" + id + ", userId=" + userId + ", canManageShop=" + canManageShop + ", canManageAdmins="
				+ canManageAdmins + ", canDeleteChatMessages=" + canDeleteChatMessages + ", canResetPassword="
				+ canResetPassword + "]";
	}
	
}
