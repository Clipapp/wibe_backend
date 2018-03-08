package com.wibe.backend.dto;

import com.wibe.backend.entities.models.Category;
import com.wibe.backend.repositories.CategoryRepository;

public class CategoryInfoDTO {
	
	private String name;
	
	private String namelan;
	
	private String description;
	
	private long numPosts;
	
	private String imgUrl;
	
	private long numFollowers;
	
	private boolean following;
	
	public CategoryInfoDTO(){
		
	}
	
	public CategoryInfoDTO(Category category, String lan){
		this.name = category.getName();
		this.description = category.getDescription();
		this.imgUrl = category.getImgUrl();
		this.namelan = getLingualName(category, lan);
	}
	
	public CategoryInfoDTO(Category category, String lan, CategoryRepository repo){
		this(category, lan);
		this.numPosts = repo.getPostCount(this.name);
		this.numFollowers = repo.getFollowerCount(this.name);
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

	public long getNumPosts() {
		return numPosts;
	}

	public void setNumPosts(long numPosts) {
		this.numPosts = numPosts;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public long getNumFollowers() {
		return numFollowers;
	}

	public void setNumFollowers(long numFollowers) {
		this.numFollowers = numFollowers;
	}

	public boolean isFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}


	public String getNamelan() {
		return namelan;
	}

	public void setNamelan(String namelan) {
		this.namelan = namelan;
	}
	
	public String getLingualName(Category c, String lan){
		String name,temp;
		switch (lan) {
		case "english":
			temp = c.getNameEnglish();
			if ( temp != null && !temp.equals("") && !temp.equals("null")){
				name = c.getNameEnglish();
			} else{
				name = c.getName();
			}
			break;
		case "hindi":
			temp = c.getNameHindi();
			if ( temp != null && !temp.equals("") && !temp.equals("null")){
				name = c.getNameHindi();
			} else{
				name = c.getName();
			}
			break;
		case "hinglish":
			temp = c.getNameHinglish();
			if ( temp != null && !temp.equals("") && !temp.equals("null")){
				name = c.getNameHinglish();
			} else{
				name = c.getName();
			}
			break;
		default:
			name = c.getName();
			break;
		}
		return name;
	}

}
