package com.wibe.backend.entities.QueryResults;

import org.neo4j.ogm.annotation.typeconversion.DateLong;
import org.springframework.data.neo4j.annotation.QueryResult;

import com.wibe.backend.dto.WibeInfoDTO;
import com.wibe.backend.entities.models.User;
import com.wibe.backend.entities.models.Wibe;

import java.util.Date;

@QueryResult
public class Notification {
	
	@DateLong
	private Date time;
	
	private String notifType;
	
	private String message;
	
	private String username;
	
	private long wid;
	
	private long uid;
	
	private String userThumbnail;
	
	private String wibeThumbnail;
	
	private boolean subscribed;
	
	public Notification(){
		
	}
	
	public Notification(User user, Wibe wibe){
		this(user);
		this.wid = wibe.getWibeId();
		this.wibeThumbnail = wibe.getThumbnail();
	}
	
	public Notification(User user, WibeInfoDTO wibe){
		this(user);
		this.wid = wibe.getWibeId();
		this.wibeThumbnail = wibe.getThumbnail();
	}
	
	public Notification(User user){
		this.uid = user.getUserId();
		this.userThumbnail = user.getImgUrl();
		this.username = user.getUsername();
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getNotifType() {
		return notifType;
	}

	public void setNotifType(String notifType) {
		this.notifType = notifType;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getWid() {
		return wid;
	}

	public void setWid(long wid) {
		this.wid = wid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getUserThumbnail() {
		return userThumbnail;
	}

	public void setUserThumbnail(String userThumbnail) {
		this.userThumbnail = userThumbnail;
	}

	public String getWibeThumbnail() {
		return wibeThumbnail;
	}

	public void setWibeThumbnail(String wibeThumbnail) {
		this.wibeThumbnail = wibeThumbnail;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isSubscribed() {
		return subscribed;
	}

	public void setSubscribed(boolean subscribed) {
		this.subscribed = subscribed;
	}

}
