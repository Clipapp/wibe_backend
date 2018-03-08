package com.wibe.backend.services;

import java.util.HashMap;
import java.util.List;

import com.wibe.backend.dto.CategoryInfoDTO;
import com.wibe.backend.dto.TagInfoDTO;
import com.wibe.backend.dto.UserInfoDTO;
import com.wibe.backend.dto.WibeDTO;
import com.wibe.backend.entities.QueryResults.Leader;
import com.wibe.backend.entities.QueryResults.Notification;
import com.wibe.backend.requests.Message;
import com.wibe.backend.responses.StandardResponse;

public interface SearchService {

	List<TagInfoDTO> suggestByTag(String text, int limit, int page, String lan);
	
	List<UserInfoDTO> suggestUser(String text, int limit, int page, long uid);
	
	List<UserInfoDTO> suggestFollowers(String token, long uid, String q, 
			int limit, int page);
	
	List<UserInfoDTO> suggestFollowings(String token, String q, int limit, int page);
	
	List<UserInfoDTO> getLikedBy(String token, long wid, int limit, int page);
	
	List<Message> suggestChats(String token, String q, int limit, int page);
	
	List<CategoryInfoDTO> getCategories(String lan);
	
	List<WibeDTO> getRecentPosts(long uid, long offset, int limit, String tag, String cat);
	
	List<WibeDTO> getPopularWibes(long offset, int limit, long uid, String tag, String cat);
	
	List<String> getChallenges(String lan);
	
	List<Leader> getLeaderBoard(int page, int limit, String token);
	
	List<Notification> getNotifications(long uid, long page, long limit, String lan);
	
	List<TagInfoDTO> getPopularTags(long page, long limit, String lan);
	
	HashMap<String , String> getWords(String lan);
	
	StandardResponse isUsernameAvailable(String name, String token);
	
	StandardResponse checkVersion(String v);
	
	public List<String> suggestUsername(String q, int limit);

}
