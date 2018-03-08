package com.wibe.backend.entities.models;

import java.util.Set;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.relationships.Comments;

//Don't forget to update UserRequest after adding new variables in this class
@NodeEntity(label="User")
public class User {
	
	@GraphId Long id;
	
	@Property(name="userId")
	private Long userId;
	
	private String version;
	
	@Property(name = "isPrivate")
	private boolean isPrivate = false;
	
	private boolean isBlocked = false;
	
	@Property(name = "source")
	private String source;
	
	@Property(name = "commentsRestricted")
	private boolean commentsRestricted = false;
	
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
	
	private boolean notif;
	
	private boolean notifLike;
	
	private boolean notifComment;
	
	private boolean notifChat;
	
	private boolean notifFollow;
	
	private boolean notifViralPosts;
	
	private long zeroTime;
	
	private String communityLan;
	
	private List<String> contentLan = new ArrayList<String>();
	
	private long seed;
	
	@DateLong
	@Property(name="createdAt")
	private Date createdAt;
	
	@DateLong
	@Property(name="lastActive")
	private Date lastActive;
	
	@Relationship(type="UPLOAD")
	private Set<Wibe> uploads;
	
	
	@Relationship(type="FAVOURITE")
	private Set<Wibe> favourites;
	
	@Relationship(type="LIKED")
	private Set<Wibe> liked;
	
	@Relationship( type = "NOTIFY", direction = Relationship.INCOMING)
	private Set<User> initiators;
	
	@Relationship(type= "NOTIFY")
	private Set<User> initiatees;
	
	@Relationship( type = "MESSAGED", direction = Relationship.INCOMING)
	private Set<User> receiver;
	
	@Relationship(type= "MESSAGED")
	private Set<User> sender;
	
	@Relationship(type="SUBSCRIBED")
	private Set<User> subscribed;
	
	@Relationship(type = "SUBSCRIBED", direction = Relationship.INCOMING)
	private Set<User> subscribers;
	
	@Relationship(type="BLOCKED")
	private Set<User> blocked;
	
	@Relationship(type = "BLOCKED", direction = Relationship.INCOMING)
	private Set<User> blockedBy;
	
	@Relationship(type="COMMENT")
	private Set<Comments> comments;
	
	@Relationship(type = "TAGGED", direction = Relationship.INCOMING)
	private Set<Wibe> tagged;
	
	@Relationship(type = "SHARED")
	private Set<Wibe> shared;
	
	@Relationship(type = "VIEWED")
	private Set<Wibe> viewed;
	
	@Relationship(type = "DOWNLOADED")
	private Set<Wibe> downloaded;
	
	@Relationship(type = "FOLLOW")
	private Set<Category> categories;
	
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

	public User(){
		
	}
	
	public User(Long userId, String number, String token){
		this.userId = userId;
		this.number = number;
		this.token = token;
	}
	
	public void setUserId(Long userId){
		this.userId = userId;
	}
	
	public Long getUserId(){
		return this.userId;
	}
	
	public void setUsername(String username){
		this.username = username.toLowerCase();
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public void setToken(String token){
		this.token = token;
	}
	
	public String getToken(){
		return this.token;
	}
	
	public void setImgUrl(String imgUrl){
		this.imgUrl = imgUrl;
	}
	
	public String getImgUrl(){
		return this.imgUrl;
	}
	
	public void setNumber(String number){
		this.number = number;
	}
	
	public String getNumber(){
		return this.number;
	}
	
	public void setFavourites(Set<Wibe> favourites){
		this.favourites = favourites;
	}
	
	public Set<Wibe> getFavourites(){
		return this.favourites;
	}
	
	public void setSubscribed(Set<User> subscribed){
		this.subscribed = subscribed;
	}
	
	public Set<User> getSubscribed(){
		return this.subscribed;
	}
	
	public void setLiked(Set<Wibe> liked){
		this.liked = liked;
	}
	
	public Set<Wibe> getLiked(){
		return this.liked;
	}

	public Set<Comments> getComments() {
		return comments;
	}

	public void setComments(Set<Comments> comments) {
		this.comments = comments;
	}

	public Set<Wibe> getTagged() {
		return tagged;
	}

	public void setTagged(Set<Wibe> tagged) {
		this.tagged = tagged;
	}

	public Set<User> getSubscribers() {
		return subscribers;
	}

	public void setSubscribers(Set<User> subscribers) {
		this.subscribers = subscribers;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name == null){
			return;
		}
		this.name = name.trim();
		if (name != null){
			this.nameLower = this.name.toLowerCase();
		}
		String[] names = this.name.split(" ");
		if (names.length ==1){
			this.firstName = names[0];
			this.lastName = "";
		} else if (names.length >1){
			this.firstName = names[0];
			this.lastName = this.name.substring(this.name.indexOf(" ") + 1);
		} else {
			this.firstName = "";
			this.lastName = "";
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		if (this.name == null){
			this.name = "";
		}
		String[] names = this.name.split(" ");
		names[0] = firstName;
		this.name = String.join(" ", names);
		if (this.username == null){
			this.username = this.firstName.toLowerCase() + this.userId.toString();
		}
		if (name != null){
			this.nameLower = this.name.toLowerCase();
		}
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		if (lastName == null){
			lastName = "";
		}
		this.lastName = lastName;
		if (this.name != null){
			String[] names = this.name.split(" ");
			names[1] = lastName;
			this.name = String.join(" ", names);
		}
		if (name != null){
			this.nameLower = this.name.toLowerCase();
		}
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

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public Set<Wibe> getUploads() {
		return uploads;
	}

	public void setUploads(Set<Wibe> uploads) {
		this.uploads = uploads;
	}

	public Set<Wibe> getShared() {
		return shared;
	}

	public void setShared(Set<Wibe> shared) {
		this.shared = shared;
	}

	public Set<Wibe> getViewed() {
		return viewed;
	}

	public void setViewed(Set<Wibe> viewed) {
		this.viewed = viewed;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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

	public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getFcmToken() {
		return fcmToken;
	}

	public void setFcmToken(String fcmToken) {
		this.fcmToken = fcmToken;
	}

	public Set<User> getInitiatees() {
		return initiatees;
	}

	public void setInitiatees(Set<User> initiatees) {
		this.initiatees = initiatees;
	}

	public Set<User> getInitiators() {
		return initiators;
	}

	public void setInitiators(Set<User> initiators) {
		this.initiators = initiators;
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

	public Set<Wibe> getDownloaded() {
		return downloaded;
	}

	public void setDownloaded(Set<Wibe> downloaded) {
		this.downloaded = downloaded;
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
