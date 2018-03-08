package com.wibe.backend.library;

import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.wibe.backend.entities.models.Counter;
import com.wibe.backend.entities.models.User;
import com.wibe.backend.repositories.CounterRepository;
import com.wibe.backend.repositories.UserRepository;
import com.wibe.backend.repositories.WordRepository;
import com.wibe.backend.requests.Contact;
import com.wibe.backend.responses.StandardResponse;

public final class UserLib {
	private static UserLib userlib;
	
	private UserLib(){
		
	}
	
	public static UserLib getInstance(){
		if (userlib == null){
			userlib = new UserLib();
		}
		return userlib;
	}
	
	public void sendSMSMessage(AmazonSNSClient snsClient, String message,
			String phoneNumber, Map<String, MessageAttributeValue> smsAttributes) {
		PublishResult result = snsClient.publish(new PublishRequest()
                    .withMessage(message)
                    .withPhoneNumber(phoneNumber)
                    .withMessageAttributes(smsAttributes));
		System.out.println(result); // Prints the message ID.
	}
	
	public Long getNextCount(CounterRepository counterRepository, String name){
        Counter obj = counterRepository.findAndIncreaseByCounterId(name);
        return obj.getSeq();
	}
	
	public String getUuid(String id) {
		   // creating UUID      
		   UUID uid = UUID.randomUUID();  
		   return uid.toString();
	}  
	
	public StandardResponse updateContacts(List<Contact> contacts, long uid,
			UserRepository userRepo, WordRepository wordRepo){
		try{
			ExecutorService executor = Executors.newFixedThreadPool(25);
			for (Contact c : contacts){
				Runnable worker = new Runnable(){
					public void run(){
						if (userRepo.verifySubscriptionViaNumber(uid, c.getNumber())==0) {
							User u = userRepo.subscribeContact(c.getNumber(), uid);
							if (u != null) {
								Notifications.notifyFollow(uid, u.getUserId(), userRepo, wordRepo);
							}
						}
					}
				};
				executor.execute(worker);
			}
			executor.shutdown();
		} catch (Exception e){
			e.printStackTrace();
			return new StandardResponse(false);
		}
		return new StandardResponse(true);
	}
	
	public static List<String> suggestUsername(String name, UserRepository userRepo, int num){
		List<String> suggestions = new ArrayList<String> ();
			if (name.length() < 4) {
				name = getRandomName(name);
			}
			int count = userRepo.getUsernameCount(name);
			int numSuggestions = num;
			while (numSuggestions > 0) {
				while (userRepo.countUsernames(name + Integer.toString(count)) != 0) {
					count++;
				}
				suggestions.add(name + Integer.toString(count));
				numSuggestions-- ;
			}
		return suggestions;
	}

	public static String getRandomName(String name) {
		final String alphabets = "abcdefghijklmnopqrstuvwxyz";
		Random r = new Random();
		if (name.length() > 4) {
			return name;
		} else {
			while (name.length() < 5) {
				name = name + alphabets.charAt(r.nextInt(alphabets.length()));
			}
		}
		return name;
	}
}
