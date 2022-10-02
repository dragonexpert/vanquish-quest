package com.revature.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.revature.exceptions.ArmorNotFoundException;
import com.revature.exceptions.NoPermissionException;
import com.revature.exceptions.UserAlreadyAdminException;
import com.revature.exceptions.UserNotAdminException;
import com.revature.exceptions.WeaponNotFoundException;
import com.revature.repository.entity.AdminUser;
import com.revature.repository.entity.Armor;
import com.revature.repository.entity.Weapon;
import com.revature.service.AdminUserService;
import com.revature.service.ArmorService;
import com.revature.service.WeaponService;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

	private AdminUserService adminService;
	private WeaponService weaponService;
	private ArmorService armorService;


	
	public AdminController(AdminUserService adminService, WeaponService weaponService, ArmorService armorService) {
		this.adminService = adminService;
		this.weaponService = weaponService;
		this.armorService = armorService;
	}

	@GetMapping("/{id}/view")
	public @ResponseBody ResponseEntity<?> getAdmins(
			@PathVariable("id") long user_id) throws NoPermissionException
	{
		if(!adminService.isAdmin(user_id))
		{
			throw new NoPermissionException();
		}
		return ResponseEntity.ok(adminService.findAll());
	}
	
	@PutMapping("/{id}/admin/add")
	public @ResponseBody ResponseEntity<?> addAdmin(
			@PathVariable("id") long id,
			@RequestParam(name = "user_id", required = true) long user_id,
			@RequestParam(name = "shop", required = false) Optional<Boolean> shop,
			@RequestParam(name = "chat", required = false) Optional<Boolean> chat,
			@RequestParam(name = "password", required = false) Optional<Boolean> password
			) throws NoPermissionException, UserAlreadyAdminException
	{
		if(!adminService.isAdmin(id))
		{
			throw new NoPermissionException();
		}
		if(adminService.isAdmin(user_id))
		{
			throw new UserAlreadyAdminException();
		}
		AdminUser currentUserPermissions = adminService.findByUserId(id);
		if(!currentUserPermissions.isCanManageAdmins())
		{
			throw new NoPermissionException();
		}
		AdminUser au = new AdminUser();
		au.setUserId(user_id);
		au.setCanDeleteChatMessages(chat.orElse(false));
		au.setCanManageShop(shop.orElse(false));
		// This permission must be granted exclusively using the database
		au.setCanManageAdmins(false);
		au.setCanResetPassword(password.orElse(false));
		return ResponseEntity.status(201).body(adminService.save(au));
	}
	
	@DeleteMapping("/{id}/admin/delete")
	public @ResponseBody ResponseEntity<?> deleteAdmin(
			@PathVariable("id") long id,
			@RequestParam(name = "user_id", required = true) long user_id) throws NoPermissionException, UserNotAdminException
	{
		if(!adminService.isAdmin(id))
		{
			throw new NoPermissionException();
		}
		if(!adminService.isAdmin(user_id))
		{
			throw new UserNotAdminException();
		}
		AdminUser currentUserPermissions = adminService.findByUserId(id);
		if(!currentUserPermissions.isCanManageAdmins())
		{
			throw new NoPermissionException();
		}
		adminService.deleteByUserId(user_id);
		return ResponseEntity.status(204).body("");
	}
	
	@PutMapping("/{id}/weapon/add")
	public @ResponseBody ResponseEntity<?> addWeapon(
			@PathVariable("id") long id,
			@RequestParam(name = "name", required = true) String name,
			@RequestParam(name = "strength", required = true) int strength,
			@RequestParam(name = "cost", required = true) int cost) throws NoPermissionException
	{
		if(!adminService.isAdmin(id))
		{
			throw new NoPermissionException();
		}
		AdminUser au = adminService.findByUserId(id);
		if(!au.isCanManageShop())
		{
			throw new NoPermissionException();
		}
		Weapon weapon = new Weapon();
		weapon.setName(name);
		weapon.setStrength(strength);
		weapon.setCost(cost);
		return ResponseEntity.status(201).body(weaponService.save(weapon));
	}
	
	@PutMapping(value="/{id}/weapon/update")
	public ResponseEntity<?> updateWeapon(
			@PathVariable("id") long id,
			@RequestParam(name = "id", required = true) long weapon_id,
			@RequestParam(name="name", required=false) Optional<String> name,
			@RequestParam(name="strength", required=false) Optional<Integer> strength,
			@RequestParam(name="cost", required=false) Optional<Integer> cost
			) throws WeaponNotFoundException, NoPermissionException {
		if(!adminService.isAdmin(id))
		{
			throw new NoPermissionException();
		}
		AdminUser au = adminService.findByUserId(id);
		if(!au.isCanManageShop())
		{
			throw new NoPermissionException();
		}
		
		Optional<Weapon> oldWeapon = weaponService.findById(weapon_id);
		if(!oldWeapon.isPresent())
		{
			throw new WeaponNotFoundException();
		}
	Weapon weapon = new Weapon();
	weapon.setWeapon_id(weapon_id);
	weapon.setName(name.orElse(oldWeapon.get().getName()));
	weapon.setStrength(strength.orElse(oldWeapon.get().getStrength()));
	weapon.setCost(cost.orElse(oldWeapon.get().getCost()));
	
	Weapon dbWeapon = weaponService.save(weapon);
	
	JSONObject object = new JSONObject();
	object.appendField("weapon_id", dbWeapon.getWeapon_id());
	object.appendField("name", dbWeapon.getName());
	object.appendField("strength", dbWeapon.getStrength());
	object.appendField("cost", dbWeapon.getCost());
	object.appendField("url", "/weapon/" + dbWeapon.getWeapon_id());
	return ResponseEntity.ok(object);
	}
	
	@DeleteMapping("/{id}/weapon/delete")
	public @ResponseBody ResponseEntity<?> deleteWeapon(
			@PathVariable("id") long id,
			@RequestParam(name = "id", required = true) long weapon_id) throws NoPermissionException, WeaponNotFoundException
	{
		if(!adminService.isAdmin(id))
		{
			throw new NoPermissionException();
		}
		AdminUser au = adminService.findByUserId(id);
		if(!au.isCanManageShop())
		{
			throw new NoPermissionException();
		}
		if(!weaponService.exists(weapon_id))
		{
			throw new WeaponNotFoundException();
		}
		weaponService.deleteById(weapon_id);
		return ResponseEntity.status(204).body("");
	}
	
	
	@PutMapping("/{id}/armor/add")
	public @ResponseBody ResponseEntity<?> addArmor(
			@PathVariable("id") long id,
			@RequestParam(name = "name", required = true) String name,
			@RequestParam(name = "defense", required = true) int defense,
			@RequestParam(name = "cost", required = true) int cost) throws NoPermissionException
	{
		if(!adminService.isAdmin(id))
		{
			throw new NoPermissionException();
		}
		AdminUser au = adminService.findByUserId(id);
		if(!au.isCanManageShop())
		{
			throw new NoPermissionException();
		}
		Armor armor = new Armor();
		armor.setName(name);
		armor.setDefense(defense);
		armor.setCost(cost);
		return ResponseEntity.status(201).body(armorService.save(armor));
	}
	
	@PutMapping(value="/{id}/armor/update")
	public ResponseEntity<?> updateArmor(
			@PathVariable("id") long id,
			@RequestParam(name = "id", required = true) long armor_id,
			@RequestParam(name="name", required=false) Optional<String> name,
			@RequestParam(name="defense", required=false) Optional<Integer> defense,
			@RequestParam(name="cost", required=false) Optional<Integer> cost
			) throws ArmorNotFoundException, NoPermissionException 
	{
		if(!adminService.isAdmin(id))
		{
			throw new NoPermissionException();
		}
		AdminUser au = adminService.findByUserId(id);
		if(!au.isCanManageShop())
		{
			throw new NoPermissionException();
		}
		
		Optional<Armor> oldArmor = armorService.findById(armor_id);
		if(!oldArmor.isPresent())
		{
			throw new ArmorNotFoundException();
		}
		Armor armor = new Armor();
		armor.setArmor_id(armor_id);
		armor.setName(name.orElse(oldArmor.get().getName()));
		armor.setDefense(defense.orElse(oldArmor.get().getDefense()));
		armor.setCost(cost.orElse(oldArmor.get().getCost()));
	
		Armor dbArmor = armorService.save(armor);
	
		JSONObject object = new JSONObject();
		object.appendField("armor_id", dbArmor.getArmor_id());
		object.appendField("name", dbArmor.getName());
		object.appendField("defense", dbArmor.getDefense());
		object.appendField("cost", dbArmor.getCost());
		object.appendField("url", "/armor/" + dbArmor.getArmor_id());
		return ResponseEntity.ok(object);
	}
	
	@DeleteMapping("/{id}/armor/delete")
	public @ResponseBody ResponseEntity<?> deleteArmor(
			@PathVariable("id") long id,
			@RequestParam(name = "id", required = true) long armor_id) throws NoPermissionException, ArmorNotFoundException
	{
		if(!adminService.isAdmin(id))
		{
			throw new NoPermissionException();
		}
		AdminUser au = adminService.findByUserId(id);
		if(!au.isCanManageShop())
		{
			throw new NoPermissionException();
		}
		if(!armorService.exists(armor_id))
		{
			throw new ArmorNotFoundException();
		}
		armorService.deleteById(armor_id);
		return ResponseEntity.status(204).body("");
	}
	
	@ExceptionHandler(NoPermissionException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public Object onNoPermissionException() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.appendField("error_code", 403);
		jsonObject.appendField("error_message", "You do not have access to this resource.");
		jsonObject.appendField("error_cause", "You clicked on a resource you do not have access to.");
		jsonObject.appendField("date", LocalDate.now());
		jsonObject.appendField("time", LocalTime.now());
		return jsonObject;
	}
	
	@ExceptionHandler(UserNotAdminException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Object onUserNotAdminException() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.appendField("error_code", 400);
		jsonObject.appendField("error_message", "The user is not an admin.");
		jsonObject.appendField("error_cause", "You attempted to remove a user that is not an admin from the admin group.");
		jsonObject.appendField("date", LocalDate.now());
		jsonObject.appendField("time", LocalTime.now());
		return jsonObject;
	}
	
	@ExceptionHandler(WeaponNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Object onWeaponNotFoundException() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.appendField("error_code", 404);
		jsonObject.appendField("error_message", "The weapon does not exist.");
		jsonObject.appendField("error_cause", "You navigated directly to the page instead of using a link.");
		jsonObject.appendField("date", LocalDate.now());
		jsonObject.appendField("time", LocalTime.now());
		return jsonObject;
	}
	
	@ExceptionHandler(ArmorNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public Object onArmorNotFoundException() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.appendField("error_code", 404);
		jsonObject.appendField("error_message", "The specified armor does not exist.");
		jsonObject.appendField("error_cause", "Make sure to request the proper armor.");
		jsonObject.appendField("date", LocalDate.now());
		jsonObject.appendField("time", LocalTime.now());
		return jsonObject;
	}
	
	/*
	 * Uncomment this once we know the module is stable because you won't get the direct message to show up otherwise.
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Object onException() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.appendField("error_code", 400);
		jsonObject.appendField("error_message", "There was an error processing the request.");
		jsonObject.appendField("error_cause", "A general exception occurred that doesn't have a specific handler.");
		jsonObject.appendField("date", LocalDate.now());
		jsonObject.appendField("time", LocalTime.now());
		return jsonObject;
	}
	*/
}
