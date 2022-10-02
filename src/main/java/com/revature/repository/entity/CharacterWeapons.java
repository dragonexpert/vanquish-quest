package com.revature.repository.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "character_weapons")
public class CharacterWeapons {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "character_id")
	private long characterId;
	
	@Column(name = "weapon_id")
	private long weaponId;

	public CharacterWeapons() {
		super();
	}

	public CharacterWeapons(long id, long characterId, long weapon_id) {
		this.id = id;
		this.characterId = characterId;
		this.weaponId = weapon_id;
	}
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

	public long getCharacterId() {
		return characterId;
	}

	public void setCharacter_id(long characterId) {
		this.characterId = characterId;
	}

	public long getWeaponId() {
		return weaponId;
	}

	public void setWeapon_id(long weapon_id) {
		this.weaponId = weapon_id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(characterId, id, weaponId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CharacterWeapons other = (CharacterWeapons) obj;
		return characterId == other.characterId && id == other.id && weaponId == other.weaponId;
	}

	@Override
	public String toString() {
		return "CharacterWeapons [id=" + id + ", character_id=" + characterId + ", weaponId=" + weaponId + "]";
	}
	
}
