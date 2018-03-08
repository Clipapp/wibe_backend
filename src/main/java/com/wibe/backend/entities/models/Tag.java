package com.wibe.backend.entities.models;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Labels;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

@NodeEntity
public class Tag {

	@GraphId Long id;
	
	private long tagId;
	
	private String name;
	
	private String nameEnglish;
	
	private String nameHindi;
	
	private String nameHinglish;
	
	private String description;
	
	private long wibeCount;
	
	private boolean isChallenge = false;
	
	private boolean active = false;
	
	@DateLong
	private Date createdAt;
	
	@Relationship(type = "HASHTAG", direction = Relationship.INCOMING)
	private List<Wibe> wibes;
	
	@Labels
	private List<String> labels = new ArrayList<String>();

	public long getTagId() {
		return tagId;
	}

	public void setTagId(long tagId) {
		this.tagId = tagId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Wibe> getWibes() {
		return wibes;
	}

	public void setWibes(List<Wibe> wibes) {
		this.wibes = wibes;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
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

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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

	public long getWibeCount() {
		return wibeCount;
	}

	public void setWibeCount(long wibeCount) {
		this.wibeCount = wibeCount;
	}


}
