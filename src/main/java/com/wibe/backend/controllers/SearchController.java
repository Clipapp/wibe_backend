package com.wibe.backend.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.wibe.backend.dto.CategoryInfoDTO;
import com.wibe.backend.dto.TagInfoDTO;
import com.wibe.backend.dto.UserInfoDTO;
import com.wibe.backend.dto.WibeDTO;
import com.wibe.backend.entities.QueryResults.Leader;
import com.wibe.backend.entities.QueryResults.Notification;
import com.wibe.backend.exceptions.TypeNotFoundException;
import com.wibe.backend.requests.Message;
import com.wibe.backend.responses.StandardResponse;
import com.wibe.backend.services.SearchServiceImpl;
import com.wibe.backend.services.UserServiceImpl;

@Controller
public class SearchController {
	
	private final SearchServiceImpl searchService;
	private final UserServiceImpl userService;
	
	@Autowired
	public SearchController(SearchServiceImpl searchService, UserServiceImpl userService){
		this.searchService = searchService;
		this.userService = userService;
		
	}
	
	@RequestMapping(value = "suggest/tag", method = RequestMethod.GET)
	public @ResponseBody List<TagInfoDTO> suggestTag(@RequestParam(value = "q") String q,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
			@RequestParam(value= "lan", required= false, defaultValue= "")String lan){
		if (limit > 10 || limit < 1){
			limit = 10;
		}
		return searchService.suggestByTag(q.trim().toLowerCase(), limit, page, lan);
	}
	
	@RequestMapping(value = "suggest/user", method = RequestMethod.GET)
	public @ResponseBody List<UserInfoDTO> suggestUser(@RequestParam(value = "q") String q,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
			@RequestParam(value = "uid") long uid){
		if (limit > 10 || limit < 1){
			limit = 10;
		}
		return searchService.suggestUser(q.trim().toLowerCase(), limit, page, uid);
	}
	
	@RequestMapping(value = "suggest/username", method = RequestMethod.GET)
	public @ResponseBody List<String> suggestUsername(@RequestParam(value = "q") String q,
			@RequestParam(value = "limit", required = false, defaultValue = "3") int limit){
		if (limit > 10 || limit < 1){
			limit = 10;
		}
		return searchService.suggestUsername(q.trim().toLowerCase(), limit);
	}
	
	@RequestMapping(value = "suggest/followers", method = RequestMethod.GET)
	public @ResponseBody List<UserInfoDTO> suggestFollowers(@RequestParam(value = "q") String q,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int limit,
			@RequestHeader("token") String token,
			@RequestParam(value = "uid") long uid){
		if (limit > 10 || limit < 1){
			limit = 10;
		}
		return searchService.suggestFollowers(token, uid, q.trim().toLowerCase(), limit, page);
	}
	
	@RequestMapping(value = "suggest/followings", method = RequestMethod.GET)
	public @ResponseBody List<UserInfoDTO> suggestFollowings(@RequestParam(value = "q") String q,
			@RequestHeader("token") String token,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int limit){
		if (limit > 10 || limit < 1){
			limit = 10;
		}
		return searchService.suggestFollowings(token, q.trim().toLowerCase(), limit, page);
	}
	
	@RequestMapping(value = "wibes/{wid}/likedby", method = RequestMethod.GET)
	public @ResponseBody List<UserInfoDTO> getLikedBy(
			@RequestHeader("token") String token,
			@PathVariable("wid") long wid,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "10") int limit){
		if (limit > 18 || limit < 1){
			limit = 18;
		}
		return searchService.getLikedBy(token, wid, limit, page);
	}
	
	@RequestMapping(value = "suggest/chats", method = RequestMethod.GET)
	public @ResponseBody List<Message> suggestChats(@RequestParam(value = "q") String q,
			@RequestHeader("token") String token,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "5") int limit){
		if (limit > 10 || limit < 1){
			limit = 10;
		}
		return searchService.suggestChats(token, q.trim().toLowerCase(), limit, page);
	}
	
	@RequestMapping(value = "challenges", method = RequestMethod.GET)
	public @ResponseBody List<String> getChallenges(
			@RequestParam(value = "lan", required =false, defaultValue="english")String lan){
		return searchService.getChallenges(lan.toLowerCase());
	}
	
	@RequestMapping(value = "categories", method = RequestMethod.GET)
	public @ResponseBody List<CategoryInfoDTO> getCategories(
			@RequestParam(value = "lan", required= false, defaultValue="english") String lan){
		return searchService.getCategories(lan.toLowerCase());
	}
	
	@RequestMapping(value = "wibes/{type}", method = RequestMethod.GET)
	public @ResponseBody List<WibeDTO> getRecentWibes(
			@PathVariable("type") String type,
			@RequestParam(value = "category", required = false, defaultValue= "") String cat,
			@RequestParam(value = "tag", required = false, defaultValue = "") String tag,
			@RequestParam(value = "uid") long uid,
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "25")int limit){
		if (limit> 25 || limit < 1){
			limit =25;
		}
		if (type.equals("recent")){
			return searchService.getRecentPosts(uid, page * limit, limit, tag.toLowerCase(), 
					cat.toLowerCase());
		} else if (type.equals("popular")){
			return searchService.getPopularWibes(page * limit, limit, uid, 
					tag.toLowerCase(), cat.toLowerCase());
		} else {
			throw new TypeNotFoundException();
		}
	}
	
	@RequestMapping(value = "leaderboard", method = RequestMethod.GET)
	public @ResponseBody List<Leader> getLeaderBoard(
			@RequestParam(value = "page", required = false, defaultValue = "0") int page,
			@RequestParam(value = "limit", required = false, defaultValue = "25")int limit,
			@RequestHeader("token") String token){
		if (limit > 25 || limit < 1){
			limit = 25;
		}
		if ((page-1) * limit > 100){
			return new ArrayList<Leader>();
		}
		if ( page *limit > 100){
			page = ((int) 100/limit) + 1;
			limit = 100 - (page-1)*limit;
		}
		return searchService.getLeaderBoard(page, limit, token);
	}
	
	@RequestMapping(value = "wibers/{uid}/notifications", method = RequestMethod.GET)
	public @ResponseBody List<Notification> getNotifcations(
			@PathVariable("uid") long uid,
			@RequestParam(value = "lan", required = false, defaultValue = "english") String lan,
			@RequestParam(value = "page", required = false, defaultValue= "0") long page,
			@RequestParam(value = "limit", required = false, defaultValue = "10") long limit){
		if (limit > 25 || limit < 1){
			limit = 25;
		}
		return searchService.getNotifications(uid, page, limit, lan.toLowerCase());
	}
	
	@RequestMapping(value = "tags/popular", method = RequestMethod.GET)
	public @ResponseBody List<TagInfoDTO> getPopularTags(
			@RequestParam(value = "page", required = false, defaultValue= "0") long page,
			@RequestParam(value = "limit", required = false, defaultValue = "10") long limit,
			@RequestParam(value = "lan", required = false, defaultValue = "") String lan){
		if (limit >25 || limit <1){
			limit = 25;
		}
		return searchService.getPopularTags(page, limit, lan.toLowerCase());
		
	}
	
	@RequestMapping(value = "words/{language}", method = RequestMethod.GET)
	public @ResponseBody HashMap<String, String> getWords(
			@PathVariable("language") String lan,
			@RequestHeader(value = "token", required = false) String token){
		if (token !=null)
			userService.updateLan(token, lan.toLowerCase(), 0);
		return searchService.getWords(lan.toLowerCase());
		
	}
	
	@RequestMapping(value= "username", method = RequestMethod.GET)
	public @ResponseBody StandardResponse isUsernameAvailable(
			@RequestParam(value = "q") String q,
			@RequestHeader("token") String token){
		return searchService.isUsernameAvailable(q.toLowerCase(), token);
	}
	
	@RequestMapping(value = "version", method = RequestMethod.GET)
	public @ResponseBody StandardResponse checkVersion(
			@RequestParam(value = "v") String v){
		return searchService.checkVersion(v);
	}

}
