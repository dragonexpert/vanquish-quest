package com.revature.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.revature.exceptions.ArmorCreationException;
import com.revature.exceptions.ArmorNotFoundException;
import com.revature.exceptions.NoArmorsException;
import com.revature.repository.entity.Armor;
import com.revature.service.ArmorService;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/armor")
@CrossOrigin(origins = "*")
public class ArmorController {
	 
	private ArmorService armorService;
	
	
	public ArmorController(ArmorService armorService) {
		this.armorService = armorService;
	}

	@GetMapping(value="/view", produces="application/json")
	public @ResponseBody ResponseEntity<?> getArmors(
			) throws NoArmorsException {
	
		ArrayList<Armor> armorList = armorService.findAll();
		return ResponseEntity.ok(armorList);
	}
	
	@GetMapping(value="/{id}", produces="application/json")
	public @ResponseBody ResponseEntity<?> getArmor(
			@PathVariable("id") long armor_id
			) throws ArmorNotFoundException {
		Optional<Armor> armor = armorService.findById(armor_id);
		if(armor.isPresent())
		{
			return ResponseEntity.ok(armor.get());
		}
		throw new ArmorNotFoundException("Armor not found");
	}
	
	@ExceptionHandler(NoArmorsException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public Object onNoArmorsException() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.appendField("error_code", 400);
		jsonObject.appendField("error_message", "There are no armors in the game.");
		jsonObject.appendField("error_cause", "Please have the game admin create some armors.");
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
	
	@ExceptionHandler(ArmorCreationException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public Object onArmorCreationException() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.appendField("error_code", 500);
		jsonObject.appendField("error_message", "There was a problem creating the armor.");
		jsonObject.appendField("error_cause", "Perhaps the auto increment value is wrong.");
		jsonObject.appendField("date", LocalDate.now());
		jsonObject.appendField("time", LocalTime.now());
		return jsonObject;
	}
	
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
}
