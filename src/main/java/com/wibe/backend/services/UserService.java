package com.wibe.backend.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.wibe.backend.dto.UserInfoDTO;
import com.wibe.backend.entities.models.User;
import com.wibe.backend.requests.Comment;
import com.wibe.backend.requests.Contacts;
import com.wibe.backend.requests.Message;
import com.wibe.backend.requests.UserPhoneInfo;
import com.wibe.backend.responses.FacebookUser;
import com.wibe.backend.responses.GoogleUser;
import com.wibe.backend.responses.Settings;
import com.wibe.backend.responses.StandardResponse;
import com.wibe.backend.responses.TruecallerUser;

public interface UserService {
	
	UserInfoDTO getUserInfo(long uid, String token);
	
	StandardResponse sendOTP(String number);
	
	StandardResponse verifyOTP(String number, String otp);
	
	StandardResponse verifyfbOTP(String code, String lan, String source);
	
	UserInfoDTO updateUser(UserInfoDTO user);
	
	String addFavourite(Long uid, Long wid);
	
	String removeFavourite(Long uid, Long wid);
	
	String subscribe(long uid, long sid);
	
	String unsubscribe(Long uid, Long sid);
	
	String like(Long uid, Long wid);
	
	String unlike(Long uid, Long wid);

	StandardResponse comment(Comment comment);
	
	StandardResponse uncomment(Long uid, Long wid, String cid);
	
	StandardResponse createFbUser(FacebookUser user);
	
	StandardResponse createGoogleUser(GoogleUser user);
	
	StandardResponse createTruecallerUser(TruecallerUser user);
	
	long followCat(long uid, String name);
	
	long unfollowCat(long uid, String name);
	
	StandardResponse uploadImage(MultipartFile file, String ext);
	
	StandardResponse test(String q, String k);
	
	StandardResponse updateFcm(long id, String fcmToken);
	
	StandardResponse updateViewCount(long uid, long wid); 
	
	StandardResponse createUser(User user);
	
	StandardResponse updateContacts(Contacts contacts);
	
	List<UserInfoDTO> getFollowers(long uid, long page, long limit, String token);
	
	List<UserInfoDTO> getFollowings(long uid, long page, long limit);
	
	StandardResponse message (Message message) throws Exception;
	
	Contacts getRegisteredContacts(Contacts contacts);
	
	StandardResponse blockUser(String token, long bid);
	
	StandardResponse unblockUser(String token, long bid);
	
	List<UserInfoDTO> getBlockedUsers(String token);
	
	StandardResponse restrictComments(String token, boolean val);
	
	StandardResponse report(String token, String  message);
	
	Settings getUserSettings(String token, String v);
	
	StandardResponse updateUserNumber(String token, String code);
	
	List<Message> getRecentChats(String token, int limit, int page);
	
	StandardResponse updateLan(String token, String lan, long type);
	
	StandardResponse updateNotif(String token, Settings setting);
	
	StandardResponse updateContentLan(String token, Settings setting);
	
	StandardResponse updatePhoneInfo(UserPhoneInfo info);
}
