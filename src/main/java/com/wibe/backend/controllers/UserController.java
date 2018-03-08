package com.wibe.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wibe.backend.dto.UserInfoDTO;
import com.wibe.backend.entities.models.Otp;
import com.wibe.backend.entities.models.User;
import com.wibe.backend.requests.Comment;
import com.wibe.backend.requests.Contacts;
import com.wibe.backend.requests.Fcm;
import com.wibe.backend.requests.Message;
import com.wibe.backend.requests.UserPhoneInfo;
import com.wibe.backend.responses.FacebookUser;
import com.wibe.backend.responses.GoogleUser;
import com.wibe.backend.responses.Settings;
import com.wibe.backend.responses.StandardResponse;
import com.wibe.backend.responses.TruecallerUser;
import com.wibe.backend.services.UserServiceImpl;

@Controller
public final class UserController {
	
	private final UserServiceImpl userService;
	
	@Autowired
	public UserController(UserServiceImpl userService){
		this.userService = userService;
	}
	
	@RequestMapping(value = "wibers/{uid}", method = RequestMethod.GET)
    public @ResponseBody UserInfoDTO getUserInfo(@PathVariable("uid") long uid,
    		@RequestHeader(value = "token", required = false) String token){
            return userService.getUserInfo(uid, token);
    }
	
	@RequestMapping(value = "otp/{number}", method = RequestMethod.GET)
	public @ResponseBody StandardResponse sendOTP(@PathVariable("number") String number){
		return userService.sendOTP(number);
	}
	
	@RequestMapping(value = "wibers", method = RequestMethod.POST)
	public @ResponseBody StandardResponse verifyOTP(@RequestBody Otp otp){
		return userService.verifyOTP(otp.getNumber(), otp.getOtp());
	}
	
	@RequestMapping(value = "otp/verify", method = RequestMethod.POST)
	public @ResponseBody StandardResponse verifyFbOTP(
			@RequestParam(value = "code") String code,
			@RequestParam(value = "lan") String lan,
			@RequestParam(value = "source", required = false, defaultValue="unknown") String source){
		return userService.verifyfbOTP(code, lan.toLowerCase(), source.toLowerCase());
	}
	
	@RequestMapping(value = "login/facebook", method = RequestMethod.POST)
	public @ResponseBody StandardResponse createFbUser(@RequestBody FacebookUser user){
		return userService.createFbUser(user);
	}
	
	@RequestMapping(value = "login/google", method = RequestMethod.POST)
	public @ResponseBody StandardResponse creategoogleUser(@RequestBody GoogleUser user){
		return userService.createGoogleUser(user);
	}
	
	@RequestMapping(value = "login/truecaller", method = RequestMethod.POST)
	public @ResponseBody StandardResponse createTruecallerUser(@RequestBody TruecallerUser user){
		return userService.createTruecallerUser(user);
	}
	
	@RequestMapping(value = "wibers", method = RequestMethod.PUT)
	public  @ResponseBody UserInfoDTO updateUser(@RequestBody UserInfoDTO user){
		return userService.updateUser(user);
	}
	
	@RequestMapping(value = "wibers/{uid}/favourites/{wid}", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse addFavourite(@PathVariable("uid")Long id,
			@PathVariable("wid") Long wid){
		String count = userService.addFavourite(id, wid);
		if (count == null){
			return new StandardResponse(false, "No user found with given id");
		} else {
			return new StandardResponse(true, count);
		}
	}
	
	@RequestMapping(value = "wibers/{uid}/favourites/{wid}", method = RequestMethod.DELETE)
	public @ResponseBody StandardResponse removeFavourite(@PathVariable("uid")Long id,
			@PathVariable("wid") Long wid){
		String count = userService.removeFavourite(id, wid);
		if (count == null){
			return new StandardResponse(false, "No such wiber or wibe found");
		} else {
			return new StandardResponse(true, count);
		}
	}
	
	@RequestMapping(value = "wibers/{uid}/subscribe/{id2}", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse subscribe(@PathVariable("uid") Long uid,
			@PathVariable("id2") Long sid){
		String count = userService.subscribe(uid, sid);
		if (count == null){
			return new StandardResponse(false, "No such wiber or wibe found");
		} else {
			return new StandardResponse(true, count);
		}
	}
	
	@RequestMapping(value = "wibers/{uid}/subscribe/{id2}", method = RequestMethod.DELETE)
	public @ResponseBody StandardResponse unsubscribe(@PathVariable("uid") Long uid,
			@PathVariable("id2") Long sid){
		String count = userService.unsubscribe(uid, sid);
		if (count == null){
			return new StandardResponse(false, "No such wiber or wibe found");
		} else {
			return new StandardResponse(true, count);
		}
	}
	
	@RequestMapping(value = "wibers/{uid}/like/{wid}", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse like(@PathVariable("uid") Long uid,
			@PathVariable("wid") Long wid){
		String count = userService.like(uid, wid);
		if (count == null){
			return new StandardResponse(false, "No such wiber or wibe found");
		} else {
			return new StandardResponse(true, count);
		}
	}
	
	@RequestMapping(value = "wibers/{uid}/like/{wid}", method = RequestMethod.DELETE)
	public @ResponseBody StandardResponse unlike(@PathVariable("uid") Long uid,
			@PathVariable("wid") Long wid){
		String count = userService.unlike(uid, wid);
		if (count == null){
			return new StandardResponse(false, "No such wiber or wibe found");
		} else {
			return new StandardResponse(true, count);
		}
	}
	
	@RequestMapping(value = "comments", method= RequestMethod.POST)
	public @ResponseBody StandardResponse comment(@RequestBody Comment comment){
		return userService.comment(comment);
	}
	
	@RequestMapping(value = "comments", method= RequestMethod.DELETE)
	public @ResponseBody StandardResponse uncomment(@RequestBody Comment comment){
		return userService.uncomment(comment.getUid(), comment.getWid(), comment.getId());
	}
	
	@RequestMapping (value = "wibers/{uid}/follow/{name}", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse followCat(
			@PathVariable("uid") long uid,
			@PathVariable("name") String name){
		long count  = userService.followCat(uid, name);
		return new StandardResponse(true, Long.toString(count));
	}
	
	@RequestMapping (value = "wibers/{uid}/follow/{name}", method = RequestMethod.DELETE)
	public @ResponseBody StandardResponse unfollowCat(
			@PathVariable("uid") long uid,
			@PathVariable("name") String name){
		long count  = userService.unfollowCat(uid, name);
		return new StandardResponse(true, Long.toString(count));
	}
	
	@RequestMapping (value = "wibers/profilepic", method = RequestMethod.POST)
	public @ResponseBody StandardResponse uploadProfilePic(
			@RequestParam(value = "file", required= true) MultipartFile file,
			@RequestParam(value = "ext") String ext){
		if (ext.equals("")){
			ext = file.getContentType().split("/")[1];
		}
		return userService.uploadImage(file, ext);
	}
	
	@RequestMapping (value = "test", method = RequestMethod.GET)
	public @ResponseBody StandardResponse test(
			@RequestParam(value = "q") String q,
			@RequestParam(value = "k") String k){
		return userService.test(q,k);
	}
	
	@RequestMapping (value = "updatefcm", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updateFCM(@RequestBody Fcm fcmobj){
		return userService.updateFcm(fcmobj.getId(), fcmobj.getFcmToken());
		
	}
	
	@RequestMapping (value = "wibers/{uid}/view/{wid}", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updateViewCount(
			@PathVariable("uid") long uid, @PathVariable("wid") long wid){
		return userService.updateViewCount(uid, wid);
	}
	
	@RequestMapping (value = "secret/user", method = RequestMethod.POST)
	public @ResponseBody StandardResponse createUser(
			@RequestParam(value = "key") String key,
			@RequestBody User user){
		if (!key.equals("backdoorforcreatinguser")){
			return new StandardResponse(false, "Forbidden");
		}
		return userService.createUser(user);
	}
	
	@RequestMapping (value = "user/contacts", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updateConatcts(@RequestBody Contacts contacts){
		return userService.updateContacts(contacts);
	}
	
	@RequestMapping (value = "user/contacts/registered", method = RequestMethod.POST)
	public @ResponseBody Contacts getRegisteredConatcts(@RequestBody Contacts contacts){
		return userService.getRegisteredContacts(contacts);
	}
	
	@RequestMapping(value = "wibers/{uid}/followers", method = RequestMethod.GET)
	public @ResponseBody List<UserInfoDTO> getFollowers(@PathVariable("uid") long uid,
			@RequestParam(value = "page", required = false, defaultValue = "0") long page,
			@RequestParam(value = "limit", required = false, defaultValue = "25") long limit,
			@RequestHeader("token") String token){
		if (limit > 25 || limit < 1){
			limit = 25;
		}
		return userService.getFollowers(uid, page, limit, token);
	}
	
	@RequestMapping(value = "wibers/{uid}/followings", method = RequestMethod.GET)
	public @ResponseBody List<UserInfoDTO> getFollowings(@PathVariable("uid") long uid,
			@RequestParam(value = "page", required = false, defaultValue = "0") long page,
			@RequestParam(value = "limit", required = false, defaultValue = "25") long limit){
		if (limit > 25 || limit < 1){
			limit = 25;
		}
		return userService.getFollowings(uid, page, limit);
	}
	
	@RequestMapping(value = "wibers/messages", method = RequestMethod.POST)
	public @ResponseBody StandardResponse message(@RequestBody Message message) throws Exception{
		return userService.message(message);
	}
	
	@RequestMapping(value = "wibers/blockedusers", method = RequestMethod.GET)
	public @ResponseBody List<UserInfoDTO> listBlockedUsers(
			@RequestHeader("token") String token){
		return userService.getBlockedUsers(token);
	}
	
	@RequestMapping(value = "wibers/restrictcomment", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse restricComment(
			@RequestHeader("token") String token){
		return userService.restrictComments(token, true);
	}
	
	@RequestMapping(value = "wibers/restrictcomment", method = RequestMethod.DELETE)
	public @ResponseBody StandardResponse unrestricComment(
			@RequestHeader("token") String token){
		return userService.restrictComments(token, false);
	}
	
	@RequestMapping(value = "wibers/report", method = RequestMethod.POST)
	public @ResponseBody StandardResponse report(
			@RequestHeader("token") String token,
			@RequestParam("report") String message){
		return userService.report(token, message);
	}
	
	@RequestMapping (value= "wibers/settings", method = RequestMethod.GET)
	public @ResponseBody Settings getSettings(
			@RequestHeader("token") String token,
			@RequestParam(value="q", required = false, defaultValue = "0.0.2") String v){
		return userService.getUserSettings(token, v);
	}
	
	@RequestMapping (value= "wibers/number", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse getNumber(
			@RequestHeader("token") String token,
			@RequestParam("code") String number){
		return userService.updateUserNumber(token, number);
	}
	
	@RequestMapping (value= "wibers/block/{bid}", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse blockUser(
			@RequestHeader("token") String token,
			@PathVariable("bid") long bid){
		return userService.blockUser(token, bid);
	}
	
	@RequestMapping (value= "wibers/block/{bid}", method = RequestMethod.DELETE)
	public @ResponseBody StandardResponse unblockUser(
			@RequestHeader("token") String token,
			@PathVariable("bid") long bid){
		return userService.unblockUser(token, bid);
	}	
	
	@RequestMapping (value = "wibers/chat/recent", method = RequestMethod.GET)
	public @ResponseBody List<Message> getrecentChats(
			@RequestHeader("token") String token,
			@RequestParam(value="page", required = false, defaultValue="0") int page,
			@RequestParam(value = "limit", required = false, defaultValue="25") int limit){
		if (limit > 25 || limit < 0){
			limit =25;
		}
		return userService.getRecentChats(token, limit, page);
	}
	
	@RequestMapping (value = "wibers/language/{lan}", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updateLan(
			@PathVariable("lan") String lan,
			@RequestHeader("token") String token,
			@RequestParam(value = "type", required = false, defaultValue="0") long type){
		return userService.updateLan(token, lan.toLowerCase(), type);
	}
	
	@RequestMapping (value = "wibers/settings/notif", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updateNotif(@RequestBody Settings setting,
			@RequestHeader("token") String token){
		return userService.updateNotif(token, setting);
	}
	
	@RequestMapping (value = "wibers/contentlan", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updateContentLan(@RequestBody Settings setting,
			@RequestHeader("token") String token){
		return userService.updateContentLan(token, setting);
	}
	
	@RequestMapping (value = "user/userphoneinfo", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updatePhoneInfo(@RequestBody UserPhoneInfo info){
		return userService.updatePhoneInfo(info);
	}
	
	 
}
