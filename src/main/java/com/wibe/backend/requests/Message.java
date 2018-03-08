package com.wibe.backend.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wibe.backend.entities.relationships.Messages;


@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
	
	private String chatId;
	
	private int chatType;
	
	
	///////////////////////////Wibe///////////////////////

	private long sharedWibeId;
	
	private String sharedWibeUploaderPicUrl;
	
	private String sharedWibeThumbnail;
	
	private String sharedWibeUploaderName;
	
	////////////////////////Profile//////////////////////
	
	private long sharedProfileUserId;
	
	private String sharedProfileUserPicUrl;
	
	private String sharedProfileUsername;
	
	private String sharedProfileName;
	
	/////////////////////////Receiver//////////////////////
	
	private String recUsername;
	
	private String recImgUrl;
	
	//////////////////////////////////////////////////////
	
	private long recId;
	
	private long senId;
	
	private String txtMessage;
	
	private long time;
	
	public Message(){
		
	}
	
	
	public Message(Messages m){
		this.chatType = m.getChatType();
		this.sharedWibeId = m.getSharedWibeId();
		this.sharedWibeUploaderPicUrl = m.getSharedWibeUploaderPicUrl();
		this.sharedWibeThumbnail = m.getSharedWibeThumbnail();
		this.sharedWibeUploaderName = m.getSharedWibeUploaderName();
		this.sharedProfileUserId = m.getSharedProfileUserId();
		this.sharedProfileUserPicUrl = m.getSharedProfileUserPicUrl();
		this.sharedProfileUsername = m.getSharedProfileUsername();
		this.sharedProfileName = m.getSharedProfileName();
		this.recId = m.getRecId();
		this.senId = m.getSenId();
		this.txtMessage = m.getTxtMessage();
		this.time = m.getTime().getTime();
	}


	public String getChatId() {
		return chatId;
	}


	public void setChatId(String chatId) {
		this.chatId = chatId;
	}


	public int getChatType() {
		return chatType;
	}


	public void setChatType(int chatType) {
		this.chatType = chatType;
	}


	public long getSharedWibeId() {
		return sharedWibeId;
	}


	public void setSharedWibeId(long sharedWibeId) {
		this.sharedWibeId = sharedWibeId;
	}


	public String getSharedWibeUploaderPicUrl() {
		return sharedWibeUploaderPicUrl;
	}


	public void setSharedWibeUploaderPicUrl(String sharedWibeUploaderPicUrl) {
		this.sharedWibeUploaderPicUrl = sharedWibeUploaderPicUrl;
	}


	public String getSharedWibeThumbnail() {
		return sharedWibeThumbnail;
	}


	public void setSharedWibeThumbnail(String sharedWibeThumbnail) {
		this.sharedWibeThumbnail = sharedWibeThumbnail;
	}


	public String getSharedWibeUploaderName() {
		return sharedWibeUploaderName;
	}


	public void setSharedWibeUploaderName(String sharedWibeUploaderName) {
		this.sharedWibeUploaderName = sharedWibeUploaderName;
	}


	public long getSharedProfileUserId() {
		return sharedProfileUserId;
	}


	public void setSharedProfileUserId(long sharedProfileUserId) {
		this.sharedProfileUserId = sharedProfileUserId;
	}


	public String getSharedProfileUserPicUrl() {
		return sharedProfileUserPicUrl;
	}


	public void setSharedProfileUserPicUrl(String sharedProfileUserPicUrl) {
		this.sharedProfileUserPicUrl = sharedProfileUserPicUrl;
	}


	public String getSharedProfileUsername() {
		return sharedProfileUsername;
	}


	public void setSharedProfileUsername(String sharedProfileUsername) {
		this.sharedProfileUsername = sharedProfileUsername;
	}


	public String getSharedProfileName() {
		return sharedProfileName;
	}


	public void setSharedProfileName(String sharedProfileName) {
		this.sharedProfileName = sharedProfileName;
	}


	public String getRecUsername() {
		return recUsername;
	}


	public void setRecUsername(String recUsername) {
		this.recUsername = recUsername;
	}


	public String getRecImgUrl() {
		return recImgUrl;
	}


	public void setRecImgUrl(String recImgUrl) {
		this.recImgUrl = recImgUrl;
	}


	public long getRecId() {
		return recId;
	}


	public void setRecId(long recId) {
		this.recId = recId;
	}


	public long getSenId() {
		return senId;
	}


	public void setSenId(long senId) {
		this.senId = senId;
	}


	public String getTxtMessage() {
		return txtMessage;
	}


	public void setTxtMessage(String txtMessage) {
		this.txtMessage = txtMessage;
	}


	public long getTime() {
		return time;
	}


	public void setTime(long time) {
		this.time = time;
	}




}
