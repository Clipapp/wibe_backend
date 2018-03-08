package com.wibe.backend.dto;

import com.wibe.backend.entities.models.Tag;

public class TagInfoDTO {
	
	private String name;
	
	private String nameLan;
	
	private String description;
	
	private boolean isChallenge;
	
	private long numWibes;
	
	public TagInfoDTO(){
		
	}
	
	public TagInfoDTO(Tag tag, String lan){
		this.name = tag.getName();
		this.nameLan = getLingualName(tag, lan);
		this.description = tag.getDescription();
		this.isChallenge = tag.isChallenge();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isChallenge() {
		return isChallenge;
	}

	public void setChallenge(boolean isChallenge) {
		this.isChallenge = isChallenge;
	}

	public long getNumWibes() {
		return numWibes;
	}

	public void setNumWibes(long numWibes) {
		this.numWibes = numWibes;
	}

	public String getNameLan() {
		return nameLan;
	}

	public void setNameLan(String nameLan) {
		this.nameLan = nameLan;
	}
	
	public String getLingualName(Tag t, String lan){
		String name,temp;
		switch (lan) {
		case "english":
			temp = t.getNameEnglish();
			if ( temp != null && !temp.equals("") && !temp.equals("null")){
				name = t.getNameEnglish();
			} else{
				name = t.getName();
			}
			break;
		case "hindi":
			temp = t.getNameHindi();
			if ( temp != null && !temp.equals("") && !temp.equals("null")){
				name = t.getNameHindi();
			} else{
				name = t.getName();
			}
			break;
		case "hinglish":
			temp = t.getNameHinglish();
			if ( temp != null && !temp.equals("") && !temp.equals("null")){
				name = t.getNameHinglish();
			} else{
				name = t.getName();
			}
			break;
		default:
			name = t.getName();
			break;
		}
		return name;
	}



}
