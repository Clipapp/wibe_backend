package com.wibe.backend.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wibe.backend.entities.models.User;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Settings {
	
	private String number;
	
	private boolean commentsRestricted;
	
	private boolean updateRequired;
	
	private boolean elbUrl;
	
	private boolean notif;
	
	private boolean notifLike;
	
	private boolean notifComment;
	
	private boolean notifChat;
	
	private boolean notifFollow;
	
	private boolean notifViralPosts;
	
	private List<String> contentLan;
	
	public Settings(){
		
	}
	
	public Settings(User user){
		this.number = user.getNumber();
		this.commentsRestricted = user.isCommentsRestricted();
		this.notif = !user.isNotif();
		this.notifChat = !user.isNotifChat();
		this.notifLike = !user.isNotifLike();
		this.notifComment = !user.isNotifComment();
		this.notifFollow = !user.isNotifFollow();
		this.notifViralPosts = !user.isNotifViralPosts();
		this.contentLan = user.getContentLan();
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public boolean isCommentsRestricted() {
		return commentsRestricted;
	}

	public void setCommentsRestricted(boolean commentsRestricted) {
		this.commentsRestricted = commentsRestricted;
	}

	public boolean isUpdateRequired() {
		return updateRequired;
	}

	public void setUpdateRequired(boolean updateRequired) {
		this.updateRequired = updateRequired;
	}

	public boolean isElbUrl() {
		return elbUrl;
	}

	public void setElbUrl(boolean elbUrl) {
		this.elbUrl = elbUrl;
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

	public List<String> getContentLan() {
		return contentLan;
	}

	public void setContentLan(List<String> contentLan) {
		this.contentLan = contentLan;
	}

}
