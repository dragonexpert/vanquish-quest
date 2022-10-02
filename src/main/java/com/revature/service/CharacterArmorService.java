package com.revature.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.revature.repository.ArmorRepository;
import com.revature.repository.CharacterArmorRepository;
import com.revature.repository.entity.Armor;
import com.revature.repository.entity.CharacterArmor;

@Service
public class CharacterArmorService {
	
	private CharacterArmorRepository repo;
	private ArmorRepository armorRepository;
	
	
	@Autowired
	public CharacterArmorService(CharacterArmorRepository repo, ArmorRepository armorRepository) {
		this.repo = repo;
		this.armorRepository = armorRepository;
	}

	public ArrayList<Armor> findArmorsByCharacterId(long id)
	{
		List<CharacterArmor> myList = repo.findAllByCharacterId(id);
		ArrayList<Armor> armors = new ArrayList<>();
		for(CharacterArmor o : myList)
		{
			Optional<Armor> armor = armorRepository.findById(o.getArmorId());
			if(armor.isPresent())
			{
				armors.add(armor.get());
			}
		}
		return armors;
	}
	
	public Armor addArmor(long character_id, long armor_id)
	{
		CharacterArmor armor = new CharacterArmor();
		armor.setCharacterId(character_id);
		armor.setArmorId(armor_id);
		repo.save(armor);
		Optional<Armor> armorStats = armorRepository.findById(armor_id);
		return armorStats.get();
	}
	
	public void removeArmor(long character_id, long armor_id)
	{
		Optional<CharacterArmor> ca = repo.findFirstByCharacterIdAndArmorId(character_id, armor_id);
		if(ca.isPresent())
		{
			repo.deleteById(ca.get().getId());
		}
	}

}
