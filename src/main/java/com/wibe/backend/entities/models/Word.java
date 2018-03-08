package com.wibe.backend.entities.models;

import java.lang.reflect.Field;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Word {

	@GraphId Long id;
	
	private String name;
	
	private String type;
	
	private String english;
	
	private String hindi;
	
	private String hinglish;
	
	private String marathi;
	
	private String gujarati;
	
	private String punjabi;
	
	private String malayalam;
	
	private String bengali;
	
	private String odia;
	
	private String tamil;
	
	private String telugu;
	
	private String kannada;
	
	private String urdu;
	
	public Word(){
		
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder s = new StringBuilder("");
		for (Field f : this.getClass().getDeclaredFields()){
			s.append(f.getName());
			s.append(": ");
			try {
				s.append(f.get(this));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			s.append("\n");
		}
		return s.toString();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getHindi() {
		return hindi;
	}

	public void setHindi(String hindi) {
		this.hindi = hindi;
	}

	public String getHinglish() {
		return hinglish;
	}

	public void setHinglish(String hinglish) {
		this.hinglish = hinglish;
	}

	public String getMarathi() {
		return marathi;
	}

	public void setMarathi(String marathi) {
		this.marathi = marathi;
	}

	public String getGujarati() {
		return gujarati;
	}

	public void setGujarati(String gujarati) {
		this.gujarati = gujarati;
	}

	public String getOdia() {
		return odia;
	}

	public void setOdia(String odia) {
		this.odia = odia;
	}

	public String getTamil() {
		return tamil;
	}

	public void setTamil(String tamil) {
		this.tamil = tamil;
	}

	public String getTelugu() {
		return telugu;
	}

	public void setTelugu(String telugu) {
		this.telugu = telugu;
	}

	public String getKannada() {
		return kannada;
	}

	public void setKannada(String kannada) {
		this.kannada = kannada;
	}

	public String getUrdu() {
		return urdu;
	}

	public void setUrdu(String urdu) {
		this.urdu = urdu;
	}

	public String getPunjabi() {
		return punjabi;
	}

	public void setPunjabi(String punjabi) {
		this.punjabi = punjabi;
	}

	public String getMalayalam() {
		return malayalam;
	}

	public void setMalayalam(String malayalam) {
		this.malayalam = malayalam;
	}

	public String getBengali() {
		return bengali;
	}

	public void setBengali(String bengali) {
		this.bengali = bengali;
	}

}
