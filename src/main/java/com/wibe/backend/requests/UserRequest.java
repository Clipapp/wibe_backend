package com.wibe.backend.requests;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.models.User;

public class UserRequest {
	
	@Property(name="userId")
	private Long userId;
	
	private String version;
	
	@Property(name = "isPrivate")
	private boolean isPrivate;
	
	private boolean isBlocked;
	
	private long zeroTime;
	
	@Property(name = "source")
	private String source;
	
	@Property(name = "commentsRestricted")
	private boolean commentsRestricted;
	
	@Property(name = "fcmToken")
	private String fcmToken;
	
	@Property(name = "name")
	private String name;
	
	private String nameLower;
	
	@Property(name = "firstName")
	private String firstName;
	
	@Property(name = "lastName")
	private String lastName;
	
	@Property(name = "gender")
	private String gender;
	
	@Property(name = "about")
	private String about;
	
	@Property(name = "email")
	private String email;
	
	@Property(name = "birthday")
	private String birthday;
	
	@Property(name = "location")
	private String location;
	
	@Property(name = "facebookId")
	private String facebookId;
	
	@Property(name = "googleId")
	private String googleId;
	
	@Property(name="username")
	private String username;
	
	@Property(name="token")
	private String token;
	
	@Property(name="imgUrl")
	private String imgUrl;
	
	@Property(name="number")
	private String number;
	
	@Property(name="countryCode")
	private String countryCode;
	
	@Property(name="lan")
	private String lan;
	
	@DateLong
	@Property(name="createdAt")
	private Date createdAt;
	
	@DateLong
	@Property(name="lastActive")
	private Date lastActive;
	
	private boolean notif;
	
	private boolean notifLike;
	
	private boolean notifComment;
	
	private boolean notifChat;
	
	private boolean notifFollow;
	
	private boolean notifViralPosts;
	
	private String communityLan;
	
	private List<String> contentLan;
	
	private long seed;
	
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

	public UserRequest(){
		
	}
	
	public UserRequest(User user){
		for (Field f : this.getClass().getDeclaredFields()){
			try {
				Field s = user.getClass().getDeclaredField(f.getName());
				s.setAccessible(true);
				f.set(this, s.get(user));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}

	public boolean isCommentsRestricted() {
		return commentsRestricted;
	}

	public void setCommentsRestricted(boolean commentsRestricted) {
		this.commentsRestricted = commentsRestricted;
	}

	public String getLan() {
		return lan;
	}

	public void setLan(String lan) {
		this.lan = lan;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getLastActive() {
		return lastActive;
	}

	public void setLastActive(Date lastActive) {
		this.lastActive = lastActive;
	}

	public boolean isIsBlocked() {
		return isBlocked;
	}

	public void setIsBlocked(boolean isBlocked) {
		this.isBlocked = isBlocked;
	}

	public long getZeroTime() {
		return zeroTime;
	}

	public void setZeroTime(long zeroTime) {
		this.zeroTime = zeroTime;
	}

	public boolean isNotif() {
		return notif;
	}

	public void setNotif(boolean notif) {
		this.notif = notif;
	}

	public boolean isNotifLike() {
		return notifLike;
	}

	public void setNotifLike(boolean notifLike) {
		this.notifLike = notifLike;
	}

	public boolean isNotifComment() {
		return notifComment;
	}

	public void setNotifComment(boolean notifComment) {
		this.notifComment = notifComment;
	}

	public boolean isNotifChat() {
		return notifChat;
	}

	public void setNotifChat(boolean notifChat) {
		this.notifChat = notifChat;
	}

	public boolean isNotifFollow() {
		return notifFollow;
	}

	public void setNotifFollow(boolean notifFollow) {
		this.notifFollow = notifFollow;
	}

	public boolean isNotifViralPosts() {
		return notifViralPosts;
	}

	public void setNotifViralPosts(boolean notifViralPosts) {
		this.notifViralPosts = notifViralPosts;
	}

	public String getCommunityLan() {
		return communityLan;
	}

	public void setCommunityLan(String communityLan) {
		this.communityLan = communityLan;
	}

	public List<String> getContentLan() {
		return contentLan;
	}

	public void setContentLan(List<String> contentLan) {
		this.contentLan = contentLan;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public String getNameLower() {
		return nameLower;
	}

	public void setNameLower(String nameLower) {
		this.nameLower = nameLower;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	

}
