package com.revature.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.revature.repository.entity.CharacterWeapons;

public interface CharacterWeaponsRepository extends JpaRepository<CharacterWeapons, Long> {
	public List<CharacterWeapons> findAllByCharacterId(long id);
	
	public Optional<CharacterWeapons> findFirstByCharacterIdAndWeaponId(long characterId, long weaponId);
}
