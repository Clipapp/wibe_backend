package com.wibe.backend.entities.relationships;

import java.util.Date;

import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.DateLong;

import com.wibe.backend.entities.models.User;

@RelationshipEntity(type = "MESSAGED")
public class Messages {
	
	@GraphId Long id;
	
	@StartNode
	private User sender;
	
	@EndNode
	private User receiver;
	
	@Property private int chatType;
	
	
	///////////////////////////Wibe///////////////////////

	@Property private long sharedWibeId;
	
	@Property private String sharedWibeUploaderPicUrl;
	
	@Property private String sharedWibeThumbnail;
	
	@Property private String sharedWibeUploaderName;
	
	////////////////////////Profile//////////////////////
	
	@Property private long sharedProfileUserId;
	
	@Property private String sharedProfileUserPicUrl;
	
	@Property private String sharedProfileUsername;
	
	@Property private String sharedProfileName;
	
	///////////////////////////////////////////////////
	
	@Property private long recId;
	
	@Property private long senId;
	
	@Property private String txtMessage;
	
	@DateLong
	@Property private Date time;

	public Messages(){
		
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

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	
}
