package com.wibe.backend.threads;

import java.lang.reflect.Field;
import java.util.List;

import com.wibe.backend.entities.QueryResults.Notification;
import com.wibe.backend.entities.models.User;
import com.wibe.backend.entities.models.Word;
import com.wibe.backend.repositories.UserRepository;
import com.wibe.backend.repositories.WordRepository;

public class NotificationUpdateThread implements Runnable {

	private List<Notification> notifs;
	private int pos;
	private String lan;
	private WordRepository wordRepo;
	private UserRepository userRepo;
	private long uid;
	
	public NotificationUpdateThread() {
		// TODO Auto-generated constructor stub
	}
	public NotificationUpdateThread(List<Notification> notifs, int pos, String lan,
			WordRepository wordRepo, UserRepository userRepo, long uid){
		this.notifs = notifs;
		this.pos = pos;
		this.lan = lan;
		this.wordRepo = wordRepo;
		this.userRepo = userRepo;
		this.uid = uid;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		Notification n = notifs.get(pos);
		String key ;
		switch (n.getNotifType()) {
		case "comment":
			key = "commented_on_your_wibe";
			break;
		
		case "like":
			key = "favorited_your_wibe";
			break;
		
		case "follow":
			key = "started_following_you";
			break;
			
		case "commentVideo":
			key = "comment_video";
			break;
			
		default:
			key = null;
			break;
		}
		if (key == null)
			return;
		
		Word word = wordRepo.getWordByKey(key);
		try {
			Field field = word.getClass().getDeclaredField(lan);
			field.setAccessible(true);
			n.setMessage((String)field.get(word));
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		n.setSubscribed(userRepo.verifySubscription(n.getUid(), uid) != 0);
		User u = userRepo.findByUserId(n.getUid());
		System.out.println("==========>");
		System.out.println(u.getUserId());
		System.out.println(u.getUsername());
		n.setUserThumbnail(u.getImgUrl());
		n.setUsername(u.getUsername());
		notifs.set(pos, n);
	}

}
