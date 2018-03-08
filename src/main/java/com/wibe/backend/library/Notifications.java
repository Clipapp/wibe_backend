package com.wibe.backend.library;

import java.io.IOException;
import java.lang.reflect.Field;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.wibe.backend.config.AppConfig;
//import com.wibe.backend.dto.UserInfoDTO;
import com.wibe.backend.dto.WibeInfoDTO;
import com.wibe.backend.entities.QueryResults.Notification;
import com.wibe.backend.entities.models.User;
import com.wibe.backend.entities.models.Wibe;
import com.wibe.backend.entities.models.Word;
import com.wibe.backend.repositories.UserRepository;
import com.wibe.backend.repositories.WibeRepository;
import com.wibe.backend.repositories.WordRepository;

public class Notifications {
	
	public static void notifyLike(long uid, long wid, UserRepository userRepo,
			WibeRepository wibeRepo, WordRepository wordRepo){
		
		Runnable runnable = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				User user = userRepo.findByUserId(uid);
				Wibe w = wibeRepo.findByWibeId(wid);
				WibeInfoDTO wibe = new WibeInfoDTO(w);
				User uploader = userRepo.findByUserId(wibeRepo.findByWibeId(wid).getUploaderId());
				if (uid == uploader.getUserId()){
					return;
				}
				JSONObject data = new JSONObject(new Notification(user, wibe));
				try {
					data.put("notifType", "like");
					data.put("userId", uid);
					data.put("wibeId", wid);
					data.put("tag", "like");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String message =  "liked your video";
				String  fcmMessage  = user.getUsername() + " " + 
						getLingualMessage(uploader.getLan(), "like", wordRepo);
				userRepo.updateNotification(message, "like", uid, uploader.getUserId(), wid,
						 w.getThumbnail());
				if (!(uploader.isNotif() || uploader.isNotifLike())){
					sendNotification("Clip", fcmMessage, uploader.getFcmToken(), data);
				}
			}
			
		};
		new Thread(runnable).start();
		
	}
	
	public static void notifyFollow(long uid, long upid, UserRepository userRepo,
			WordRepository wordRepo){
		
		Runnable runnable = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				User user = userRepo.findByUserId(uid);
				User uploader = userRepo.findByUserId(upid);
				if (uid == upid){
					return;
				}
				JSONObject data = new JSONObject(new Notification(user));
				try {
					data.put("notifType", "follow");
					data.put("userId", uid);
					data.put("tag", "follow");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String message = "became your fan";
				String fcmMessage = user.getUsername() + " " +  
						getLingualMessage(uploader.getLan(), "follow", wordRepo);
				userRepo.updateNotification(message, "follow", uid, upid, -1, null);
				if (!(uploader.isNotif() || uploader.isNotifFollow())){
					sendNotification("Clip", fcmMessage, uploader.getFcmToken(), data);
				}
			}
			
		};
		new Thread(runnable).start();
		
	}
	
	public static void notifyComment(long uid, long wid, UserRepository userRepo,
			WibeRepository wibeRepo, WordRepository wordRepo){
		
		Runnable runnable = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				User user = userRepo.findByUserId(uid);
				Wibe wibe = wibeRepo.findByWibeId(wid);
				JSONObject data = new JSONObject(new Notification(user, wibe));
				if (uid == wibe.getUploaderId()){
					return;
				}
				try {
					data.put("wibeId", wid);
					data.put("userId", uid);
					data.put("notifType", "comment");
					data.put("tag", "comment");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				User uploader = userRepo.findByUserId(wibeRepo.findByWibeId(wid).getUploaderId());
				String message = "commented on your video";
				String fcmMessage = user.getUsername() + " " +
						getLingualMessage(uploader.getLan(), "comment", wordRepo);
				userRepo.updateNotification(message, "comment", uid, uploader.getUserId(), wid, 
						wibe.getThumbnail());
				if (!(uploader.isNotif() || uploader.isNotifComment())){
					sendNotification("Clip", fcmMessage, uploader.getFcmToken(), data);
				}
				//List<User> commentators = wibeRepo.findCommentators(wid);
				//message = "commented on a video on which you commented";
				//for (User u : commentators){
				//	if (u.getUserId() != user.getUserId()){
				//		fcmMessage = user.getUsername() + " " +
				//				getLingualMessage(uploader.getLan(), "commentVideo", wordRepo);
				//		userRepo.updateNotification(message, "commentVideo", uid, u.getUserId(), wid, 
				//				wibe.getThumbnail());
				//		if (!(uploader.isNotif() || uploader.isNotifComment())){
				//			sendNotification("Clip", fcmMessage, u.getFcmToken(), data);
				//	}
				//}
				//}
			}
			
		};
		new Thread(runnable).start();
		
	}
	
	public static void notifyMessage(long rid, long sid ,UserRepository userRepo,
			WordRepository wordRepo){
		Runnable runnable = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				User sender = userRepo.findByUserId(sid);
				User receiver = userRepo.findByUserId(rid);
				JSONObject data = new JSONObject(new Notification(sender));
				if (rid == sid){
					return;
				}
				try {
					data.put("notifType", "message");
					data.put("userId", sid);
					data.put("tag", "message");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String message = sender.getUsername() + " " +
						getLingualMessage(receiver.getLan(), "message", wordRepo);
				//userRepo.createNotification(message, "message", sid, rid, -1, null);
				if (!(receiver.isNotif() || receiver.isNotifChat())){
					sendNotification("Clip", message, receiver.getFcmToken(), data);
				}
			}
			
		};
		new Thread(runnable).start();
	}
	
	public static void notifyDelete(long wid, UserRepository userRepo, 
			WibeRepository wibeRepo, WordRepository wordRepo){
		Runnable runnable = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Wibe wibe = wibeRepo.getByWibeId(wid);
				User user = userRepo.findByUserId(wibe.getUploaderId());
				JSONObject data = new JSONObject(new Notification(user));
				try {
					data.put("notifType", "general");
					data.put("userId", user.getUserId());
					data.put("tag", "delete");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String message = getLingualMessage(user.getLan(), "delete", wordRepo);
				sendNotification("Clip", message, user.getFcmToken(), data);
			}
			
		};
		new Thread(runnable).start();
	}
	
	public static void notifyUploadBlocked(String token, UserRepository userRepo, 
			WordRepository wordRepo){
		Runnable runnable = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				User user = userRepo.findbyToken(token);
				JSONObject data = new JSONObject(new Notification(user));
				try {
					data.put("notifType", "general");
					data.put("userId", user.getUserId());
					data.put("tag", "uploadBlocked");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String message = getLingualMessage(user.getLan(), "uploadBlocked", wordRepo);
				sendNotification("Clip", message, user.getFcmToken(), data);
			}
			
		};
		new Thread(runnable).start();
	}
	
	public static String getLingualMessage(String lan, String type, WordRepository wordRepo){
		String key, lMessage = null;
		lan = lan==null ? "english" : lan;
		switch (type) {
		case "comment":
			key = "commented_on_your_wibe";
			break;
		
		case "like":
			key = "favorited_your_wibe";
			break;
		
		case "follow":
			key = "started_following_you";
			break;
			
		case "message":
			key = "messaged_you";
			break;
			
		case "delete" :
			key = "video_deleted";
			break;
			
		case "commentVideo":
			key = "comment_video";
			break;
			
		case "uploadBlocked" :
			key = "upload_blocked";
			break;
			
		default:
			key = null;
			break;
		}
		if (key == null)
			return null;
		
		Word word = wordRepo.getWordByKey(key);
		try {
			Field field = word.getClass().getDeclaredField(lan);
			field.setAccessible(true);
			lMessage = (String)field.get(word);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			lMessage = word.getEnglish();
		}
		
		return lMessage;
	}
	
	public static void sendNotification(String title, String body, String fcmToken, 
			JSONObject data){
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
		post.setHeader("Content-type", "application/json");
		post.setHeader("Authorization", "key=" + AppConfig.FcmKey);
		JSONObject message = new JSONObject();
		try {
			message.put("to", fcmToken);
			message.put("priority", "high");
	
			//JSONObject notification = new JSONObject();
			data.put("title", title);
			data.put("body", body);
			data.put("time", System.currentTimeMillis());
	
			//message.put("notification", notification);
			
			message.put("data", data);
	
			post.setEntity(new StringEntity(message.toString(), "UTF-8"));
		} catch (Exception e){
			e.printStackTrace();
		}
		HttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpEntity entity = response.getEntity();
		String responseString = null;
		try {
			responseString = EntityUtils.toString(entity, "UTF-8");
		} catch (ParseException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(responseString);
		System.out.println(message);
		
	}
	
//	public static void main(String[] args){
//		JSONObject data = new JSONObject();
//		try {
//			data.put("notifType", "like");
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		sendNotification("Test", "hello", "cOnSVIvlNfI:APA91bFoGl5LV-Wzs2Epill1tCTLTWd9DoA9JOZanq3KWmAgtn6ScVKoKtXnBHPNVuzoHvtvAldxq2X7E5jkIvkCA2a3hARDm9sgUgVYTaabD017zhGOZdeyFJkWEBS88CG6PQcex7bA", data);
//	}

}
