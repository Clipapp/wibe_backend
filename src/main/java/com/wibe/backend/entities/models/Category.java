package com.wibe.backend.entities.models;

import java.util.List;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

@NodeEntity
public class Category {
	
	@GraphId Long id;
	
	private long categoryId;
	
	private String name;
	
	private String nameEnglish;
	
	private String nameHindi;
	
	private String nameHinglish;
	
	private String description;
	
	private String imgUrl;
	
	private boolean live;
	
	@Relationship(type = "CATEGORY", direction = Relationship.INCOMING)
	private List<Wibe> wibes;
	
	@Relationship(type = "FOLLOW", direction = Relationship.INCOMING)
	private List<User> followers;

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
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

	public List<Wibe> getWibes() {
		return wibes;
	}

	public void setWibes(List<Wibe> wibes) {
		this.wibes = wibes;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<User> getFollowers() {
		return followers;
	}

	public void setFollowers(List<User> followers) {
		this.followers = followers;
	}

	public String getNameEnglish() {
		return nameEnglish;
	}

	public void setNameEnglish(String nameEnglish) {
		this.nameEnglish = nameEnglish;
	}

	public String getNameHindi() {
		return nameHindi;
	}

	public void setNameHindi(String nameHindi) {
		this.nameHindi = nameHindi;
	}

	public String getNameHinglish() {
		return nameHinglish;
	}

	public void setNameHinglish(String nameHinglish) {
		this.nameHinglish = nameHinglish;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

}
