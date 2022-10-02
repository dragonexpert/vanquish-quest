package com.revature.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revature.repository.entity.CharacterArmor;

public interface CharacterArmorRepository extends JpaRepository<CharacterArmor, Long> {
	public List<CharacterArmor> findAllByCharacterId(long id);
	
	public Optional<CharacterArmor> findFirstByCharacterIdAndArmorId(long characterId, long armorId);


}
