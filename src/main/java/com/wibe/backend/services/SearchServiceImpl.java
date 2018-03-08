package com.wibe.backend.services;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wibe.backend.dto.CategoryInfoDTO;
import com.wibe.backend.dto.TagInfoDTO;
import com.wibe.backend.dto.UserInfoDTO;
import com.wibe.backend.dto.WibeDTO;
//import com.wibe.backend.dto.WibeInfoDTO;
import com.wibe.backend.entities.QueryResults.Leader;
import com.wibe.backend.entities.QueryResults.Notification;
import com.wibe.backend.entities.QueryResults.WibeObj;
import com.wibe.backend.entities.models.Category;
import com.wibe.backend.entities.models.Counter;
import com.wibe.backend.entities.models.Tag;
import com.wibe.backend.entities.models.User;
//import com.wibe.backend.entities.models.Wibe;
import com.wibe.backend.entities.models.Word;
import com.wibe.backend.library.TagLib;
import com.wibe.backend.library.UserLib;
import com.wibe.backend.library.UtilLib;
//import com.wibe.backend.library.WibeLib;
import com.wibe.backend.repositories.CategoryRepository;
import com.wibe.backend.repositories.CounterRepository;
import com.wibe.backend.repositories.TagRepository;
import com.wibe.backend.repositories.UserRepository;
import com.wibe.backend.repositories.WibeRepository;
import com.wibe.backend.repositories.WordRepository;
import com.wibe.backend.requests.Message;
import com.wibe.backend.responses.StandardResponse;
import com.wibe.backend.threads.NotificationUpdateThread;


@Service
public final class SearchServiceImpl implements SearchService {

	private final TagRepository tagRepository;
	private final WibeRepository wibeRepository;
	private final UserRepository userRepository;
	private final CategoryRepository catRepository;
	private final WordRepository wordRepository;
	private final CounterRepository counterRepository;
	//private final WibeLib wibelib = WibeLib.getInstance();
	private final TagLib taglib = TagLib.getInstance();
	 
	public SearchServiceImpl(TagRepository tagRepository, CategoryRepository catRepository,
			WibeRepository wibeRepository, UserRepository userRepository, 
			WordRepository wordRepository, CounterRepository counterRepository){
		this.tagRepository = tagRepository;
		this.catRepository = catRepository;
		this.wibeRepository = wibeRepository;
		this.userRepository = userRepository;
		this.wordRepository = wordRepository;
		this.counterRepository = counterRepository;
	}
	
	@Override
	public List<TagInfoDTO> suggestByTag(String text, int limit, int page, String lan) {
		// TODO Auto-generated method stub
		List <Tag> tags = tagRepository.suggestTag(text, limit, page * limit);
		List <TagInfoDTO> tagdto = new ArrayList<TagInfoDTO>(Collections.nCopies(
				tags.size(), null));
		taglib.createTagDTO(tags, tagdto, tagRepository, lan);
		return tagdto;
	}
	
	@Override
	public List<UserInfoDTO> suggestUser(String text, int limit, int page, long uid) {
		// TODO Auto-generated method stub
		List<UserInfoDTO> userdto = new ArrayList<UserInfoDTO>();
		for (User u:userRepository.suggestUser(text.toLowerCase(), limit, page *limit, uid)){
			UserInfoDTO udto = new UserInfoDTO(u); 
			udto.setSubscribed(userRepository.verifySubscription(u.getUserId(), uid) != 0);
			userdto.add(udto);
		}
		return userdto;
	}
	
	@Override
	public List<CategoryInfoDTO> getCategories(String lan) {
		// TODO Auto-generated method stub
		List<Category> categories = catRepository.getCategories();
		List<CategoryInfoDTO> categorydto = new ArrayList<CategoryInfoDTO>();
		for (Category c: categories){
			categorydto.add(new CategoryInfoDTO(c, lan));
		}
		return  categorydto;
	}
	
	@Override
	public List<WibeDTO> getRecentPosts(long uid, long offset, int limit, String tag, 
			String cat) {
		// TODO Auto-generated method stub
		List <WibeObj> wibes = null;
		if (tag.equals("") && cat.equals("")){
			try {
				User user = userRepository.findByUserId(uid);
				Counter counter = counterRepository.findByCounterId("wibeId");
				wibes = wibeRepository.getListWibes2(offset, (long) limit, counter.getSeq(), 
						user.getZeroTime(), user.getContentLan());
				List<Long> blockedUsers = userRepository.getBlockedUsersId(user.getToken());
				for (WibeObj w: wibes){
					for (Long num : blockedUsers){
						if (w.getUser().getUserId() == num.longValue()){
							wibes.remove(w);
						}
					}
				}	
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		} else if (tag.equals("") && !cat.equals("")){
			wibes = wibeRepository.getRecentPostsByCat(offset, limit, cat);
		} else if (!tag.equals("") && cat.equals("")){
			try {
				User user = userRepository.findByUserId(uid);
				wibes = wibeRepository.getRecentPostsByTag(offset, limit, tag, uid);
				List<Long> blockedUsers = userRepository.getBlockedUsersId(user.getToken());
				for (WibeObj w: wibes){
					for (Long num : blockedUsers){
						if (w.getUser().getUserId() == num.longValue()){
							wibes.remove(w);
						}
					}
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
//		List <WibeInfoDTO> wibeinfodto = new ArrayList<WibeInfoDTO>(Collections.nCopies(
//				wibes.size(), null));
//		wibelib.createWibeDTO(wibes, wibeRepository, userRepository, wibeinfodto, -1,
//				true, true, true);
		List<WibeDTO> wibedto = new ArrayList<WibeDTO>();
		for (WibeObj w :wibes){
			wibedto.add(new WibeDTO(w));
		}
		return wibedto;
	}

	@Override
	public List<String> getChallenges(String lan) {
		// TODO Auto-generated method stub
		List<Tag> challenges = tagRepository.getChallenges();
		List<String>  names = new ArrayList<String>();
		for (Tag c: challenges){
			names.add(new TagInfoDTO(c, lan).getNameLan());
		}
		return names;
	}

	@Override
	public List<WibeDTO> getPopularWibes(long offset, int limit, long uid, String tag,
			String cat) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -6);
		long time_week = cal.getTimeInMillis();
		List <WibeObj> wibes = null;
		if (tag.equals("") && cat.equals("")){
			wibes = wibeRepository.getPopularPosts(offset, limit, time_week);
		} else if (tag.equals("") && !cat.equals("")){
			wibes = wibeRepository.getPopularPostsByCat(offset, limit, time_week, cat);
		} else if (!tag.equals("") && cat.equals("")){
			wibes = wibeRepository.getPopularPostsByTag(offset, limit, time_week, tag, uid);
		}
		if (wibes == null){
			return null;
		}
//		List <WibeInfoDTO> wibeinfodto = new ArrayList<WibeInfoDTO>(Collections.nCopies(
//				wibes.size(), null));
//		wibelib.createWibeDTO(wibes, wibeRepository, userRepository, wibeinfodto, uid,
//				true, true, true);
		List<WibeDTO> wibedto = new ArrayList<WibeDTO>();
		for (WibeObj w :wibes){
			wibedto.add(new WibeDTO(w));
		}
		return wibedto;
	}

	@Override
	public List<Leader> getLeaderBoard(int page, int limit, String token) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		long time_day = cal.getTimeInMillis();
		List<Leader> leaders = userRepository.getLeaderBoard(time_day, page * limit, 
				limit, token);
		List<Long> blockedUsers = userRepository.getBlockedUsersId(token);
		for (Leader l : leaders){
			l.setSubscribed(userRepository.verifySubscription(l.getUserId(), token)!=0);
			for (Long num : blockedUsers){
				if (l.getUserId() == num.longValue()){
					leaders.remove(l);
				}
			}
		}
		return leaders;
	}

	@Override
	public List<Notification> getNotifications(long uid, long page, long limit, String lan) {
		// TODO Auto-generated method stub
		List<Notification> notifications = userRepository.getNotifications(uid, 
				page * limit , limit);
		ExecutorService executor = Executors.newFixedThreadPool(20);
		for (int i =0; i< notifications.size(); i++){
			Runnable worker = new NotificationUpdateThread(notifications, i, lan, 
					wordRepository, userRepository, uid);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()){
			
		}
		return notifications;
	}

	@Override
	public List<TagInfoDTO> getPopularTags(long page, long limit, String lan) {
		// TODO Auto-generated method stub
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -1);
		long time_day = cal.getTimeInMillis();
		List <Tag> tags = tagRepository.getPopularTags(time_day, page* limit, limit);
		List <TagInfoDTO> tagdto = new ArrayList<TagInfoDTO>(Collections.nCopies(
				tags.size(), null));
		taglib.createTagDTO(tags, tagdto, tagRepository, lan);
		return tagdto;
	}

	@Override
	public HashMap<String, String> getWords(String lan) {
		// TODO Auto-generated method stub
		HashMap<String, String> words = new HashMap<String , String>();
		List<Word> result = wordRepository.getWords();
		for (Word w: result){
			try {
				Field f = w.getClass().getDeclaredField(lan);
				f.setAccessible(true);
				words.put(w.getName(), (String) f.get(w));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				words.put(w.getName(), w.getEnglish());
			}
			
		}
		return words;
	}

	@Override
	public StandardResponse isUsernameAvailable(String name, String token) {
		// TODO Auto-generated method stub
		try{
			if(userRepository.findbyToken(token).getUsername().equals(name)) {
				return new StandardResponse(true);
			}
			return new StandardResponse(userRepository.countUsernames(name.toLowerCase()) == 0);
		} catch (Exception e){
			return new StandardResponse(false, e.toString());
		}
	}

	@Override
	public StandardResponse checkVersion(String v) {
		// TODO Auto-generated method stub
		Word version = null;
		int res;
		try {
			version = wordRepository.getWordByKey("version");
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return new StandardResponse(false, "Failed to fetch version");
		}
		if (version == null)
			return new StandardResponse(false, "Failed to fetch version");
		res = UtilLib.compareVersions(version.getEnglish(), v);
		switch (res) {
		case 1:
			return new StandardResponse(true, "Update Required");
		case 0:
			return new StandardResponse(false, "Same versions");
		case -1:
			return new StandardResponse(false, "You have updated version");
		case -2:
			return new StandardResponse(false, "Version length mismatch");
		case -3:
			return new StandardResponse(false, "Version should contain integeral tokens");
		default:
			return new StandardResponse(false);
		}
	}

	@Override
	public List<UserInfoDTO> suggestFollowers(String token, long uid, String q, int limit,
			int page) {
		// TODO Auto-generated method stub
		List<UserInfoDTO> userdto = new ArrayList<UserInfoDTO>();
		for (User u:userRepository.suggestFollowers(token, uid, q, page*limit, limit )){
			UserInfoDTO udto = new UserInfoDTO(u); 
			udto.setSubscribed(userRepository.verifySubscription(u.getUserId(), token) != 0);
			userdto.add(udto);
		}
		return userdto;
	}

	@Override
	public List<UserInfoDTO> suggestFollowings(String token, String q, int limit, int page) {
		// TODO Auto-generated method stub
		List<UserInfoDTO> userdto = new ArrayList<UserInfoDTO>();
		for (User u:userRepository.suggestFollowings(token, q, page*limit, limit)){
			UserInfoDTO udto = new UserInfoDTO(u); 
			udto.setSubscribed(userRepository.verifySubscription(u.getUserId(), token) != 0);
			userdto.add(udto);
		}
		return userdto;
	}

	@Override
	public List<Message> suggestChats(String token, String q, int limit, int page) {
		// TODO Auto-generated method stub
		List<Message> chats = new ArrayList<Message>();
		try {
			if (q.equals("") || q==null || q.equals("null")){
				for (Map<String, Object> m: userRepository.getRecentChats(token, limit, 
						page*limit)){
					System.out.println("=========>");
					System.out.println(m.keySet());
					System.out.println(m.values());
					chats.add(new ObjectMapper().convertValue(m, Message.class));
			
				}
			}else {
				for (Map<String, Object> m: userRepository.suggestChats(token, limit, 
						page*limit, q)){
					System.out.println("=========>");
					System.out.println(m.keySet());
					System.out.println(m.values());
					chats.add(new ObjectMapper().convertValue(m, Message.class));
			
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return chats;
	}

	public List<String> suggestUsername(String q, int limit) {
		// TODO Auto-generated method stub
		return UserLib.suggestUsername(q, userRepository, limit);
	}

	@Override
	public List<UserInfoDTO> getLikedBy(String token, long wid, int limit, int page) {
		// TODO Auto-generated method stub
		List<Long> friends = wibeRepository.getLikedByFreindsId(token, wid);
		List<UserInfoDTO> userdto = new ArrayList<UserInfoDTO>();
		List<User> likedBy = new ArrayList<>();
		int count = friends.size();
		if (count >= page*limit + limit) {
			likedBy = wibeRepository.getLikedByFreinds(token, wid, limit, page*limit);
		} else if (count >page*limit && count < page *limit +limit ) {
			likedBy = wibeRepository.getLikedByFreinds(token, wid, limit, page*limit);
			likedBy.addAll(wibeRepository.getLikedBy(token, wid, limit - likedBy.size(), 
					0, friends));
		}else {
			likedBy = wibeRepository.getLikedBy(token, wid, limit, page*limit - count, friends);
		}
		
		for (User u:likedBy){
			UserInfoDTO udto = new UserInfoDTO(u); 
			udto.setSubscribed(userRepository.verifySubscription(u.getUserId(), token) != 0);
			userdto.add(udto);
		}
		return userdto;
	}	

}
