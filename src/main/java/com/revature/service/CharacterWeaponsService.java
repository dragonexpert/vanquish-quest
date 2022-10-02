package com.revature.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.repository.CharacterWeaponsRepository;
import com.revature.repository.WeaponRepository;
import com.revature.repository.entity.CharacterWeapons;
import com.revature.repository.entity.Weapon;

@Service
public class CharacterWeaponsService {

	private CharacterWeaponsRepository repo;
	private WeaponRepository weaponRepository;
	
	
	@Autowired
	public CharacterWeaponsService(CharacterWeaponsRepository repo, WeaponRepository weaponRepository) {
		this.repo = repo;
		this.weaponRepository = weaponRepository;
	}

	public ArrayList<Weapon> findWeaponsByCharacterId(long id)
	{
		List<CharacterWeapons> myList = repo.findAllByCharacterId(id);
		ArrayList<Weapon> weapons = new ArrayList<>();
		for(CharacterWeapons o : myList)
		{
			Optional<Weapon> weapon = weaponRepository.findById(o.getWeaponId());
			weapons.add(weapon.get());
		}
		return weapons;
	}
	
	public Weapon addWeapon(long character_id, long weapon_id)
	{
		CharacterWeapons weapon = new CharacterWeapons();
		weapon.setCharacter_id(character_id);
		weapon.setWeapon_id(weapon_id);
		repo.save(weapon);
		Optional<Weapon> weaponStats = weaponRepository.findById(weapon_id);
		return weaponStats.get();
	}
	
	public void removeWeapon(long character_id, long weapon_id)
	{
		Optional<CharacterWeapons> cw = repo.findFirstByCharacterIdAndWeaponId(character_id, weapon_id);
		if(cw.isPresent())
		{
			repo.deleteById(cw.get().getId());
		}
	}
}
