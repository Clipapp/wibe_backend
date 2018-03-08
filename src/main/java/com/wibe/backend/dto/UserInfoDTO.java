package com.wibe.backend.dto;

import com.wibe.backend.entities.models.User;

public class UserInfoDTO {
	
	private Long userId;
	
	private String imgUrl;
	
	private boolean isPrivate;
	
	private String username;
	
	private String fullname;
	
	private String about;
	
	private String gender;
	
	private int numFavourites;
	
	private int numUploads;
	
	private int numFollowers;
	
	private int numFollowing;
	
	private int numViews;
	
	private boolean subscribed;
	
	public UserInfoDTO(){
		
	}
		
	public UserInfoDTO(User user){
		this.userId = user.getUserId();
		this.gender = user.getGender();
		this.imgUrl = user.getImgUrl();
		this.username = user.getUsername();
		this.setFullname(user.getName());
		this.numFavourites = user.getFavourites() == null ? 0 : user.getFavourites().size();
		this.numUploads = user.getUploads() == null ? 0 : user.getUploads().size();
		this.about = user.getAbout();
		this.numFollowers = user.getSubscribers() == null ? 0 : user.getSubscribers().size();
		this.numFollowing = user.getSubscribed() == null ? 0 : user.getSubscribed().size();
		this.numViews = user.getViewed() == null ? 0 : user.getViewed().size();
		this.isPrivate = user.isPrivate();
	}
	
	public UserInfoDTO(User user, boolean  subscribed){
		this(user);
		this.subscribed = subscribed;
	}
	
	public void setUserId(Long userId){
		this.userId = userId;
	}
	
	public Long getUserId(){
		return this.userId;
	}
	
	public void setImgUrl(String imageUrl){
		this.imgUrl = imageUrl;
	}
	
	public String getImgUrl(){
		return this.imgUrl;
	}
	
	public void setUsername(String username){
		this.username = username;
	}
	
	public String getUsername(){
		return this.username;
	}

	public int getNumFavourites() {
		return numFavourites;
	}

	public void setNumFavourites(int numFavourites) {
		this.numFavourites = numFavourites;
	}

	public int getNumUploads() {
		return numUploads;
	}

	public void setNumUploads(int numUploads) {
		this.numUploads = numUploads;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String description) {
		this.about = description;
	}

	public int getNumFollowers() {
		return numFollowers;
	}

	public void setNumFollowers(int numFollowers) {
		this.numFollowers = numFollowers;
	}

	public int getNumFollowing() {
		return numFollowing;
	}

	public void setNumFollowing(int numFollowing) {
		this.numFollowing = numFollowing;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public boolean isSubscribed() {
		return subscribed;
	}

	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}

	public int getNumViews() {
		return numViews;
	}

	public void setNumViews(int numViews) {
		this.numViews = numViews;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}
	

}
