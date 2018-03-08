package com.wibe.backend.entities.models;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;

@NodeEntity
public class Banner {
	
	@GraphId
	private Long id;
	
	private String tag;
	
	private String lan;
	
	private boolean active;
	
	private int duration;
	
	private String url;
	
	private int width;
	
	private int height;
	
	private long theme;
	
	private long backgroundMusic;
	
	private boolean rearCamera;
	
	public Banner(){
		
	}
	
	public Banner(String lan, String tag, String url, boolean active, int duration, int height, int width) {
		this.lan = lan;
		this.tag = tag;
		this.url = url;
		this.active = active;
		this.duration = duration;
		this.height = height;
		this.width = width;
	}
	
	public Banner(int a){
		this.tag = "test";
		this.duration = 30;
		this.url= "https://d2df764efu4nbt.cloudfront.net/static/assets/banners/200w_d.gif";
		this.width = 200;
		this.height = 84;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getLan() {
		return lan;
	}

	public void setLan(String lan) {
		this.lan = lan;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public long getTheme() {
		return theme;
	}

	public void setTheme(long theme) {
		this.theme = theme;
	}

	public long getBackgroundMusic() {
		return backgroundMusic;
	}

	public void setBackgroundMusic(long backgroundMusic) {
		this.backgroundMusic = backgroundMusic;
	}

	public boolean isRearCamera() {
		return rearCamera;
	}

	public void setRearCamera(boolean rearCamera) {
		this.rearCamera = rearCamera;
	}

	
	

}
