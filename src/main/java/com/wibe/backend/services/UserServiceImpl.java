package com.wibe.backend.services;

import java.io.IOException;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wibe.backend.config.AppConfig;
import com.wibe.backend.dto.UserInfoDTO;
import com.wibe.backend.entities.models.Otp;
import com.wibe.backend.entities.models.User;
import com.wibe.backend.entities.models.Word;
import com.wibe.backend.library.CsvReader;
import com.wibe.backend.library.Encryptor;
import com.wibe.backend.library.Http;
import com.wibe.backend.library.Notifications;
import com.wibe.backend.library.UserLib;
import com.wibe.backend.library.UtilLib;
import com.wibe.backend.repositories.CategoryRepository;
import com.wibe.backend.repositories.ContactRepository;
import com.wibe.backend.repositories.CounterRepository;
import com.wibe.backend.repositories.OtpRedisRepository;
import com.wibe.backend.repositories.UserPhoneInfoRepository;
import com.wibe.backend.repositories.UserRepository;
import com.wibe.backend.repositories.WibeRepository;
import com.wibe.backend.repositories.WordRepository;
import com.wibe.backend.requests.Comment;
import com.wibe.backend.requests.Contact;
import com.wibe.backend.requests.Contacts;
import com.wibe.backend.requests.Message;
import com.wibe.backend.requests.UserPhoneInfo;
import com.wibe.backend.requests.UserRequest;
import com.wibe.backend.responses.FacebookUser;
import com.wibe.backend.responses.FbAuthToken;
import com.wibe.backend.responses.FbAuthVerification;
import com.wibe.backend.responses.GoogleAuthResponse;
import com.wibe.backend.responses.GoogleUser;
import com.wibe.backend.responses.Settings;
import com.wibe.backend.responses.StandardResponse;
import com.wibe.backend.responses.TruecallerUser;

import org.json.JSONException;
import org.json.JSONObject;

@Service
public final class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
	private final WibeRepository wibeRepository;
	private final OtpRedisRepository otpRedisRepository;
	private final CounterRepository counterRepository;
	private final CategoryRepository categoryRepository;
	private final WordRepository wordRepository;
	private final UserPhoneInfoRepository upiRepository;
	private final ContactRepository contactRepository;
	private final UserLib userlib = UserLib.getInstance();
	private final Http http = Http.getInstance();
	
	public UserServiceImpl(UserRepository userRepository,
			WibeRepository wibeRepository,
			CategoryRepository categoryRepository,
			OtpRedisRepository otpRedisRepository,
			CounterRepository counterRepository,
			WordRepository wordRepository,
			ContactRepository contactRepository,
			UserPhoneInfoRepository upiRepo) {
		this.userRepository = userRepository;
		this.wibeRepository = wibeRepository;
		this.categoryRepository = categoryRepository;
		this.otpRedisRepository = otpRedisRepository;
		this.counterRepository = counterRepository;
		this.wordRepository = wordRepository;
		this.contactRepository = contactRepository;
		this.upiRepository = upiRepo;
	}

	@Override
	public UserInfoDTO getUserInfo(long uid, String token) {
		// TODO Auto-generated method stub
		UserInfoDTO userDTO;
		try {
			User user =null;
			if (token != null) {
				user = userRepository.getUserProfile(uid, token);
			} else {
				user = userRepository.findByUserId(uid);
			}
			userDTO = new UserInfoDTO(user);
			userDTO.setNumFavourites(userRepository.getFavouriteCount(uid));
			userDTO.setNumFollowers(userRepository.getFollowerCount(uid));
			userDTO.setNumFollowing(userRepository.getFollowingCount(uid));
			if (user.getToken().equals(token)){
				userDTO.setNumUploads(userRepository.getMyUploadCount(uid));
			} else {
				userDTO.setNumUploads(userRepository.getUploadCount(uid));
			}
			userDTO.setNumViews(userRepository.getViewCount(uid));
			if (token!= null) {
				User requestor = userRepository.findbyToken(token);
				userDTO.setSubscribed(userRepository.verifySubscription(uid, requestor.getUserId()) != 0);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println(e);
			return null;
		}
		return userDTO;
		
	}

	@Override
	public StandardResponse sendOTP(String number) {
		// TODO Auto-generated method stub
		if(!number.matches("[0-9]{10}")){
            return new StandardResponse(false, "Incorrect number.Number Should be 10 digits integer");
		}
		Random rnd = new Random();
		int otp = 100000 + rnd.nextInt(900000);
		Otp otp_obj = otpRedisRepository.findByNumber(number);
		if (otp_obj ==null){
            otpRedisRepository.save(new Otp(number, Integer.toString(otp)));
		}else{
            otp_obj.setNumber(number);
            otp_obj.setOtp(Integer.toString(otp));
            otpRedisRepository.save(otp_obj);
		}
		//otpRedis.addOTP(number, otp);
		AmazonSNSClient snsClient = new AmazonSNSClient();
		String message = "Hi Wiber, Your OTP for account generation on Wibe is " + Integer.toString(otp);
		String phoneNumber = "+91" + number;
		Map<String, MessageAttributeValue> smsAttributes =
				new HashMap<String, MessageAttributeValue>();
		//<set SMS attributes>
		userlib.sendSMSMessage(snsClient, message, phoneNumber, smsAttributes);
		return new StandardResponse(true);
	}

	@Override
	public StandardResponse verifyOTP(String number, String otp) {
		// TODO Auto-generated method stub
		if(!number.matches("[0-9]{10}")){
            return new StandardResponse(false, "Incorrect number");
		}
		if(otpRedisRepository.findByNumber(number) == null){
			return new StandardResponse(false, "OTP expired");
		}
		if(!otpRedisRepository.findByNumber(number).getOtp().equals(otp)){
			return new StandardResponse(false, "Wrong OTP");
		}
		try {
            User user = userRepository.findByNumber(number);
            otpRedisRepository.delete(new Otp(number, otp));
            if(user == null){
            	Long uid = userlib.getNextCount(counterRepository, "userId");
            	String uuid = userlib.getUuid(uid.toString());
            	user = new User(uid, number, uuid);
            	user = userRepository.create(user);
            	return new StandardResponse(true, user.getToken() + "," + user.getUserId());
            } else {
            	return new StandardResponse(true, user.getToken() + "," + user.getUserId());
            }
		} catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
            return new StandardResponse(false, "failed to create user");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public UserInfoDTO updateUser(UserInfoDTO user) {
		// TODO Auto-generated method stub
		User doc = userRepository.findByUserId(user.getUserId());
		if (doc == null){
			return null;
		}
		if (user.getUsername() != null){
			if (!doc.getUsername().equals(user.getUsername())) {
				if (userRepository.countUsernames(user.getUsername().toLowerCase()) == 0) {
					doc.setUsername(user.getUsername().toLowerCase());
				} else {
					doc.setUsername(UserLib.suggestUsername(user.getUsername().toLowerCase(), 
						userRepository, 1).get(0));
				}
			}
		}
		if (user.getImgUrl() != null){
			doc.setImgUrl(user.getImgUrl());
		}
		if (user.getAbout() != null){
			doc.setAbout(user.getAbout());
		}
		if (user.getFullname() != null){
			doc.setName(user.getFullname());
		}
		if (user.getGender() != null){
			doc.setGender(user.getGender());
		}
		userRepository.update(doc.getUserId(), new ObjectMapper().convertValue(
				new UserRequest(doc), Map.class));
		return new UserInfoDTO(doc);
	}

	@Override
	public String addFavourite(Long uid, Long wid) {
		// TODO Auto-generated method stub
		User user = userRepository.addFavourite(uid, wid);
		if (user == null){
			return null;
		}
		Notifications.notifyLike(uid, wid, userRepository, wibeRepository, wordRepository);
		return Integer.toString(wibeRepository.getFavouriteCount(wid));
	}

	@Override
	public String removeFavourite(Long uid, Long wid) {
		// TODO Auto-generated method stub
		User user = userRepository.removeFavourite(uid, wid);
		if (user == null){
			return null;
		}
		return Integer.toString(wibeRepository.getFavouriteCount(wid));
	}

	@Override
	public String subscribe(long uid, long sid) {
		// TODO Auto-generated method stub
		if (uid == sid){
			return null;
		}
		User user = userRepository.subscribe(uid, sid);
		if (user == null){
			return null;
		}
		Notifications.notifyFollow(uid, sid, userRepository, wordRepository);
		return Integer.toString(userRepository.getFollowerCount(sid));
	}

	@Override
	public String unsubscribe(Long uid, Long sid) {
		// TODO Auto-generated method stub
		User user = userRepository.unsubscribe(uid, sid);
		if (user == null){
			return null;
		}
		try {
			userRepository.deleteNotification("became your fan", "follow", uid, sid, -1, null);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return Integer.toString(userRepository.getFollowerCount(sid));
	}

	@Override
	public String like(Long uid, Long wid) {
		// TODO Auto-generated method stub
		User user = userRepository.like(uid, wid);
		if (user == null){
			return null;
		}
		Notifications.notifyLike(uid, wid, userRepository, wibeRepository, wordRepository);
		return Integer.toString(wibeRepository.getLikeCount(wid));
	}

	@Override
	public String unlike(Long uid, Long wid) {
		// TODO Auto-generated method stub
		User user = userRepository.unlike(uid, wid);
		if (user == null){
			return null;
		}
		return Integer.toString(wibeRepository.getLikeCount(wid));
	}

	@Override
	public StandardResponse comment(Comment comment) {
		// TODO Auto-generated method stub
		Long wid = comment.getWid();
		String url = AppConfig.firebaseDb + "/comments/" + wid.toString() + ".json?auth="
				+ AppConfig.firebaseSecret;
		JSONObject headers = new JSONObject();
		try {
			headers.put("Content-Type", "application/json");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject body = new JSONObject();
		try {
			body.put("imgUrl", comment.getImgUrl());
			body.put("username", comment.getUsername());
			body.put("message", comment.getMessage());
			body.put("uid", comment.getUid());
			body.put("time", comment.getTime());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Comment res = null;
		String cid = null;
		String serverres = Http.post(url, headers, body);
		try {
			
			res = (Comment)UtilLib.fromJsonToJava(serverres, Comment.class);
			cid = res.getName();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (cid == null){
			return new StandardResponse(false, "Failed to communicate with firebase");
		}
		User user = userRepository.comment(comment.getUid(), wid, cid);
		if (user == null){
			return new StandardResponse(false, "User not found");
		}
		Notifications.notifyComment(comment.getUid(), wid, 
				userRepository, wibeRepository, wordRepository);
		return new StandardResponse(true, Integer.toString(wibeRepository.getCommentCount(wid)));
	}

	@Override
	public StandardResponse uncomment(Long uid, Long wid, String cid) {
		// TODO Auto-generated method stub
		String url = AppConfig.firebaseDb + "/comments/" + wid.toString() + "/" +
		 cid + ".json?auth=" + AppConfig.firebaseSecret;
		JSONObject headers = new JSONObject();
		String res = Http.delete(url, headers);
		if (!(res == null || res.equals("null"))){
			return new StandardResponse(false, "Something went wrong with firebase");
		}
		User user = userRepository.uncomment(uid, wid, cid);
		if (user == null){
			return new StandardResponse(false, "User not found or Comment does not exists");
		}
		return new StandardResponse(true, 
				Integer.toString(wibeRepository.getCommentCount(wid)));
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse createFbUser(FacebookUser fbUser) {
		// TODO Auto-generated method stub
		User user;
		if (fbUser.getFacebookId() == null){
			return new StandardResponse(false,"FacebookId is required");
		}
		try {
			String res = http.sendGet("https://graph.facebook.com/v2.8/me?fields=id,name,"
					+ "birthday,email,gender,location&access_token=" + fbUser.getToken());
			FacebookUser jsonRes = (FacebookUser) UtilLib.fromJsonToJava(res, FacebookUser.class);
			if (!jsonRes.getId().equals(fbUser.getFacebookId())){
				return new StandardResponse(false, "Id and token Mismatch");
			}
			user = userRepository.findByFbId(jsonRes.getId());
	
			if (user == null){
				user = new User();
				if (fbUser.getSource() != null)
					user.setSource(fbUser.getSource());
				user.setUserId(userlib.getNextCount(counterRepository, "userId"));
				user.setToken(userlib.getUuid(user.getUserId().toString()));
			}
			
			if (fbUser.getLan() != null){
				user.setLan(fbUser.getLan().toLowerCase());
				if (user.getLan().equals("hinglish") || user.getLan().equals("english")
						|| user.getLan().equals("hindi")){
					if (!user.getContentLan().contains("hindi")){
						user.getContentLan().add("hindi");
					}
					user.setCommunityLan("hindi");
				} else {
					if (!user.getContentLan().contains("hindi")){
						user.getContentLan().add("hindi");
					}
					if (!user.getContentLan().contains(user.getLan())){
						user.getContentLan().add(user.getLan());
					}
					user.setCommunityLan(user.getLan());
				}
			} else {
				if (!user.getContentLan().contains("hindi")){
					user.getContentLan().add("hindi");
				}
				user.setCommunityLan("hindi");
			}
			if (jsonRes.getEmail() != null){
				user.setEmail(jsonRes.getEmail());
			}
			if (jsonRes.getName() != null){
				if (user.getName() == null || user.getName().equals(""))
					user.setName(jsonRes.getName());
				if (user.getUsername() == null) {
					if (userRepository.countUsernames(user.getFirstName().toLowerCase()) == 0){
						user.setUsername(user.getFirstName());
					}else {
						user.setUsername(UserLib.suggestUsername(user.getFirstName().toLowerCase(), 
								userRepository, 1).get(0));
					}
				}
			}
			if (jsonRes.getId() != null) {
				user.setFacebookId(jsonRes.getId());
			}
			if (jsonRes.getGender() != null) {
				if (user.getGender() == null || user.getGender().equals(""))
					user.setGender(jsonRes.getGender());
			}
			if (fbUser.getAbout() != null){
				if (user.getAbout()==null || user.getAbout().equals(""))
					user.setAbout(fbUser.getAbout());
			}
			if (fbUser.getImgUrl() != null){
				if (user.getImgUrl() == null || user.getImgUrl().equals(""))
					user.setImgUrl(fbUser.getImgUrl());
			}
			if (jsonRes.getBirthday() != null){
				user.setBirthday(jsonRes.getBirthday());
			}
			if (jsonRes.getLocation() != null){
				if (jsonRes.getLocation().getName() != null){
					user.setLocation(jsonRes.getLocation().getName());
				}
			}
			userRepository.update(user.getUserId(), new ObjectMapper().convertValue(
					new UserRequest(user), Map.class));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, "Invalid Fb Token or db failure");
		}
		
		return new StandardResponse(true, user.getToken() + "," + user.getUserId(), 
				new UserInfoDTO(user));
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse createGoogleUser(GoogleUser googleUser) {
		// TODO Auto-generated method stub
		User user;
		if (googleUser.getId() == null){
			return new StandardResponse(false,"Email is required");
		}
		try {
			String res = http.sendGet("https://www.googleapis.com/oauth2/v3/tokeninfo?"
					+ "id_token=" + googleUser.getIdToken());
			GoogleAuthResponse jsonUser = (GoogleAuthResponse) UtilLib.fromJsonToJava(res, 
					GoogleAuthResponse.class); 
			if (!jsonUser.getEmail().equals(googleUser.getEmail())){
				return new StandardResponse(false, "Id and Token mismatch");
			}
			user = userRepository.findByGoogleId(jsonUser.getSub());
			if (user == null){
				user = new User();
				if (googleUser.getSource() != null){
					user.setSource(googleUser.getSource());
				}
				user.setUserId(userlib.getNextCount(counterRepository, "userId"));
				user.setToken(userlib.getUuid(user.getUserId().toString()));
			}
			user.setEmail(jsonUser.getEmail());
			
			if (googleUser.getLan() != null){
				user.setLan(googleUser.getLan().toLowerCase());
				if (user.getLan().equals("hinglish") || user.getLan().equals("english")
						|| user.getLan().equals("hindi")){
					if (!user.getContentLan().contains("hindi")){
						user.getContentLan().add("hindi");
					}
					user.setCommunityLan("hindi");
				} else {
					if (!user.getContentLan().contains("hindi")){
						user.getContentLan().add("hindi");
					}
					if (!user.getContentLan().contains(user.getLan())){
						user.getContentLan().add(user.getLan());
					}
					user.setCommunityLan(user.getLan());
				}
			} else {
				if (!user.getContentLan().contains("hindi")){
					user.getContentLan().add("hindi");
				}
				user.setCommunityLan("hindi");
			}
			
			if (jsonUser.getName() != null){
				if (user.getName() == null || user.getName().equals(""))
					user.setName(jsonUser.getName());
				if (user.getUsername() == null) {
					if (userRepository.countUsernames(user.getFirstName().toLowerCase()) == 0){
						user.setUsername(user.getFirstName());
					}else {
						user.setUsername(UserLib.suggestUsername(user.getFirstName().toLowerCase(), 
								userRepository, 1).get(0));
					}
				}
			}
			if (jsonUser.getPicture() !=null){
				if (user.getImgUrl() == null || user.getImgUrl().equals(""))
					user.setImgUrl(jsonUser.getPicture());
			}
			if (jsonUser.getSub() != null){
				user.setGoogleId(jsonUser.getSub());
			}
			userRepository.update(user.getUserId(), new ObjectMapper().convertValue(
					new UserRequest(user), Map.class));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, "failed to access db");
		}
		return new StandardResponse(true, user.getToken() + "," + user.getUserId(),
				new UserInfoDTO(user));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse createTruecallerUser(TruecallerUser Tuser) {
		// TODO Auto-generated method stub
		User user;
		if (new Date().getTime() - Tuser.getTimestamp() > 10000) {
			return new StandardResponse(false,"Invalid Request");
		}
		if (Tuser.getPhoneNumber() == null){
			return new StandardResponse(false,"number is required");
		}
		try {
			if (!Tuser.getHash().equals(Encryptor.encode(AppConfig.appSecret, Tuser.getPhoneNumber() + ", " + 
					Long.toString(Tuser.getTimestamp())))){
				return new StandardResponse(false, "Invalid Request");
			}
			String countryPrefix = CsvReader.getInstance().getPhoneCode(Tuser.getCountryCode());
			String number = Tuser.getPhoneNumber().replaceAll("\\+" + countryPrefix, "");
			user = userRepository.findByNumber(number);
			if (user == null){
				user = new User();
				if (Tuser.getSource() != null){
					user.setSource(Tuser.getSource());
				}
				user.setUserId(userlib.getNextCount(counterRepository, "userId"));
				user.setToken(userlib.getUuid(user.getUserId().toString()));
				user.setNumber(number);
				user.setCountryCode(countryPrefix);
			}
			user.setEmail(Tuser.getEmail());
			
			if (Tuser.getLan() != null){
				user.setLan(Tuser.getLan().toLowerCase());
				if (user.getLan().equals("hinglish") || user.getLan().equals("english")
						|| user.getLan().equals("hindi")){
					if (!user.getContentLan().contains("hindi")){
						user.getContentLan().add("hindi");
					}
					user.setCommunityLan("hindi");
				} else {
					if (!user.getContentLan().contains("hindi")){
						user.getContentLan().add("hindi");
					}
					if (!user.getContentLan().contains(user.getLan())){
						user.getContentLan().add(user.getLan());
					}
					user.setCommunityLan(user.getLan());
				}
			} else {
				if (!user.getContentLan().contains("hindi")){
					user.getContentLan().add("hindi");
				}
				user.setCommunityLan("hindi");
			}
			
			if (Tuser.getName() != null){
				if (user.getName() == null || user.getName().equals(""))
					user.setName(Tuser.getName());
				if (user.getUsername() == null) {
					if (userRepository.countUsernames(user.getFirstName().toLowerCase()) == 0){
						user.setUsername(user.getFirstName());
					}else {
						user.setUsername(UserLib.suggestUsername(user.getFirstName().toLowerCase(), 
								userRepository, 1).get(0));
					}
				}
			}
			if (Tuser.getAvatarUrl() !=null){
				if (user.getImgUrl() == null || user.getImgUrl().equals(""))
					user.setImgUrl(Tuser.getAvatarUrl());
			}
			if (Tuser.getFacebookId() != null){
				user.setFacebookId(Tuser.getFacebookId());
			}
			userRepository.update(user.getUserId(), new ObjectMapper().convertValue(
					new UserRequest(user), Map.class));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, "failed to access db");
		}
		return new StandardResponse(true, user.getToken() + "," + user.getUserId(),
				new UserInfoDTO(user));
	}

	@Override
	public long followCat(long uid, String name) {
		// TODO Auto-generated method stub
		categoryRepository.followCat(uid, name);
		return categoryRepository.getFollowerCount(name);
	}
	
	@Override
	public long unfollowCat(long uid, String name) {
		// TODO Auto-generated method stub
		categoryRepository.unfollowCat(uid, name);
		return categoryRepository.getFollowerCount(name);
	}

	@Override
	public StandardResponse uploadImage(MultipartFile file, String ext) {
		// TODO Auto-generated method stub
		String fileType = file.getContentType();
		System.out.println(fileType);
		if (!fileType.startsWith("image")){
			return new StandardResponse(false, "File format is not supported.");
		}
		
		String key = UtilLib.getKey(counterRepository, "imgId");
		String bucket = "wibeimages";
		//String[] keys = key.split("/");
		//Long imgId = Long.parseLong(keys[keys.length-1]);
		String key_thumb = key + "_thumb." + ext;
		key = key + "." + ext;
		String url = AppConfig.imageThumbDomain + key_thumb;
	
		try {
			InputStream is = file.getInputStream();
			//PutObjectResult response = wibelib.uploadToS3(bucket, key, is, 
			//		new ObjectMetadata());
			UtilLib.uploadToS3(bucket, key, is, new ObjectMetadata());
		} catch(IOException e){
			return new StandardResponse(false, e.toString());
		}
		long time = System.currentTimeMillis();
		long currTime =0;
		while(currTime - time < 1000){
			currTime = System.currentTimeMillis();
		}
		return new StandardResponse(true, url);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse verifyfbOTP(String code, String lan, String source) {
		// TODO Auto-generated method stub
		String authUrl = AppConfig.fbAuthTokenUrl + "?grant_type=authorization_code&"
				+ "code=" + code + "&access_token=AA|" + AppConfig.fbAppId + "|" + 
				AppConfig.fbAppSecret;
		FbAuthToken authRes = null;
		FbAuthVerification fbres = null;
		try {
			String res = http.sendGet(authUrl);
			authRes = (FbAuthToken) UtilLib.fromJsonToJava(res, FbAuthToken.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (authRes == null){
			return new StandardResponse(false, "Failed to retrieve userinfo from fb");
		}
		//return new StandardResponse(true, authRes.getAccess_token());
		try {
			String appsecret_proof = UtilLib.HmacSHA256(authRes.getAccess_token(), 
					AppConfig.fbAppSecret.getBytes("UTF8"));
			String verifyUrl = AppConfig.fbOTPVerificationUrl + "?appsecret_proof=" + 
					appsecret_proof + "&access_token=" + authRes.getAccess_token();
			String res = http.sendGet(verifyUrl);
			fbres = (FbAuthVerification) UtilLib.fromJsonToJava(res, FbAuthVerification.class);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fbres == null){
			return null;
		}
		String number = fbres.getPhone().getNational_number();

		try {
            User user = userRepository.findByNumber(number);
            if(user == null){
	            	Long uid = userlib.getNextCount(counterRepository, "userId");
	            	String uuid = userlib.getUuid(uid.toString());
	            	user = new User(uid, number, uuid);
	            	user.setUsername(UserLib.suggestUsername("", userRepository, 1).get(0));
	            	user.setCountryCode(fbres.getPhone().getCountry_prefix());
	            	user.setLan(lan.toLowerCase());
	            	user.setCommunityLan(user.getLan());
	            	if (user.getLan().equals("hinglish") || user.getLan().equals("english")
							|| user.getLan().equals("hindi")){
						if (!user.getContentLan().contains("hindi")){
							user.getContentLan().add("hindi");
						}
						user.setCommunityLan("hindi");
					} else {
						if (!user.getContentLan().contains("hindi")){
							user.getContentLan().add("hindi");
						}
						if (!user.getContentLan().contains(user.getLan())){
							user.getContentLan().add(user.getLan());
						}
					}
	            	if (source != null)
	            		user.setSource(source);
	            	user = userRepository.create(user);
	            	return new StandardResponse(true, user.getToken() + "," + user.getUserId(),
	            			new UserInfoDTO(user));
            } else {
            	user.setLan(lan.toLowerCase());
            	userRepository.update(user.getUserId(),new ObjectMapper().convertValue(
            			new UserRequest(user), Map.class));
            	return new StandardResponse(true, user.getToken() + "," + user.getUserId(),
            			new UserInfoDTO(user));
            }
		} catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
            return new StandardResponse(false, "failed to create user");
		}
	}

	@Override
	public StandardResponse test(String q, String k) {
		// TODO Auto-generated method stub
//		Runnable not = new Runnable(){
//			public void run(){
//				Notifications.sendNotification("Surprise",q , k, null);
//			}
//		};
//		new Thread(not).start();
		User u = userRepository.findByUserId((long)0);
		return new StandardResponse(true, "test", u);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse updateFcm(long id, String fcmToken) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUserId(id);
		if (user == null){
			return new StandardResponse(false, "No such user Found");
		}
		try{
			user.setFcmToken(fcmToken);
			userRepository.update(user.getUserId(), new ObjectMapper().convertValue(
					new UserRequest(user), Map.class));
		} catch (Exception e){
			e.printStackTrace();
			return new StandardResponse(false, e.toString());
		}
		return new StandardResponse(true, "Updated Successfully");
	}

	@Override
	public StandardResponse updateViewCount(long uid, long wid) {
		// TODO Auto-generated method stub
		try {
//			Wibe wibe = wibeRepository.findByWibeId(wid);
//			if (wibe.getUploaderId() == uid)
//				return new StandardResponse(false, "Same user");
			userRepository.updateViewCount(uid, wid);
			return new StandardResponse(true);
		} catch (Exception e){
			e.printStackTrace();
		}
		return new StandardResponse(false);
	}

	@Override
	public StandardResponse createUser(User u) {
		// TODO Auto-generated method stub
		try {
            User user = userRepository.findByNumber(u.getNumber());
            if(user == null){
            	user = u;
            	Long uid = userlib.getNextCount(counterRepository, "userId");
            	String uuid = userlib.getUuid(uid.toString());
            	user.setUserId(uid);
            	user.setToken(uuid);
            	user = userRepository.create(user);
            	return new StandardResponse(true, user.getToken() + "," + user.getUserId());
            } else {
            	return new StandardResponse(true, user.getToken() + "," + user.getUserId());
            }
		} catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
            return new StandardResponse(false, "failed to create user");
		}
	}

	@Override
	public StandardResponse updateContacts(Contacts contacts) {
		// TODO Auto-generated method stub
		try {
			contactRepository.save(contacts);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return userlib.updateContacts(contacts.getContacts(), contacts.getUid(), userRepository, 
				wordRepository);
	}

	@Override
	public List<UserInfoDTO> getFollowers(long uid, long page, long limit, String token) {
		// TODO Auto-generated method stub
		List<User> followers = userRepository.getFollowers(uid, page*limit, limit);
		List<UserInfoDTO> followersDto = new ArrayList<UserInfoDTO>();
		User user = userRepository.findbyToken(token);
		for (User u: followers){
			UserInfoDTO userdto = new UserInfoDTO(u);
			userdto.setSubscribed(userRepository.verifySubscription(
					u.getUserId(), user.getUserId())!=0);
			followersDto.add(userdto);
		}
		return followersDto;
	}
	
	@Override
	public List<UserInfoDTO> getFollowings(long uid, long page, long limit) {
		// TODO Auto-generated method stub
		List<User> followers = userRepository.getFollowings(uid, page*limit, limit);
		List<UserInfoDTO> followersDto = new ArrayList<UserInfoDTO>();
		for (User u: followers){
			UserInfoDTO userdto = new UserInfoDTO(u);
			followersDto.add(userdto);
			userdto.setSubscribed(true);
		}
		return followersDto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse message(Message message) throws Exception {
		// TODO Auto-generated method stub
		
		long sid = message.getSenId();
		long rid = message.getRecId();
		String key = null;
		if (message.getRecId() < message.getSenId()){
			key = Encryptor.encrypt(AppConfig.messageKeySecret, AppConfig.messageIVector, 
					Long.toString(rid) + Long.toString(sid));
		} else {
			key = Encryptor.encrypt(AppConfig.messageKeySecret, AppConfig.messageIVector, 
					Long.toString(sid) + Long.toString(rid));
		}
		String url = AppConfig.firebaseDb + "/messages/" + key + ".json?auth="
				+ AppConfig.firebaseSecret;
		JSONObject headers = new JSONObject();
		try {
			headers.put("Content-Type", "application/json");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject body = new JSONObject();
		try {
			String encryptedMessage = Encryptor.encrypt(AppConfig.messageSecret, 
					AppConfig.messageIVector, message.getTxtMessage());
			for (int i = 0; i< message.getClass().getDeclaredFields().length; i++){
				Field field = message.getClass().getDeclaredFields()[i];
				field.setAccessible(true);
				try{
					String fieldName = field.getName();
					body.put(fieldName, field.get(message));
				} catch (Exception e){}
			}
			body.put("txtMessage", encryptedMessage);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Comment res = null;
		String cid = null;
		String serverres = Http.post(url, headers, body);
		try {
			
			res = (Comment)UtilLib.fromJsonToJava(serverres, Comment.class);
			cid = res.getName();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (cid == null){
			return new StandardResponse(false, "Failed to communicate with firebase");
		}
		
		Message m = new ObjectMapper().convertValue(userRepository.message(sid, rid, 
				new ObjectMapper().convertValue(message, Map.class)), Message.class);
		Notifications.notifyMessage(rid, sid, userRepository, wordRepository);
		return new StandardResponse(true, "Message Sent", m);
	}

	@Override
	public Contacts getRegisteredContacts(Contacts contacts) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newFixedThreadPool(25);
		for (Contact c : contacts.getContacts()){
			Runnable worker = new Runnable(){
				public void run(){
					User user = userRepository.findByNumber(c.getNumber());
					c.setRegistered(user != null);
				}
			};
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()){
			
		}
		return contacts;
	}

	@Override
	public StandardResponse blockUser(String token, long bid) {
		// TODO Auto-generated method stub
		long uid;
		try{
			 uid = userRepository.findbyToken(token).getUserId();
		} catch (Exception e){
			return new StandardResponse(false, e.toString());
		}
		if (uid == bid)
		{
			return new StandardResponse(false, "Cannot block yourself");
		}
		try{
			userRepository.blockUser(uid, bid);
		} catch (Exception e){
			return new StandardResponse(false, e.toString());
		}
		return new StandardResponse(true);
	}
	
	@Override
	public StandardResponse unblockUser(String token, long bid) {
		// TODO Auto-generated method stub
		try{
			long uid = userRepository.findbyToken(token).getUserId();
			userRepository.unblockUser(uid, bid);
		} catch (Exception e){
			return new StandardResponse(false, e.toString());
		}
		return new StandardResponse(true);
	}

	@Override
	public List<UserInfoDTO> getBlockedUsers(String token) {
		// TODO Auto-generated method stub
		List <User> users = null;
		List <UserInfoDTO> userdto = new ArrayList<UserInfoDTO>();
		try {
			users = userRepository.getBlockedUsers(token);
			for (User u: users){
				userdto.add(new UserInfoDTO(u));
			}
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		return userdto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse restrictComments(String token, boolean val) {
		// TODO Auto-generated method stub
		try {
			User ul = userRepository.findbyToken(token);
			ul.setCommentsRestricted(val);
			userRepository.update(ul.getUserId(), new ObjectMapper().convertValue(
					new UserRequest(ul), Map.class));
			return new StandardResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, e.toString());
		}
	}
	
	public StandardResponse report(String token, String message){
		User user = userRepository.findbyToken(token);
		Long uid = user.getUserId();
		String url = AppConfig.firebaseDb + "/reports/" + uid.toString() + ".json?auth="
				+ AppConfig.firebaseSecret;
		JSONObject headers = new JSONObject();
		try {
			headers.put("Content-Type", "application/json");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JSONObject body = new JSONObject();
		try {
			body.put("report", message);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Comment res = null;
		String cid = null;
		try {
			String serverres = Http.post(url, headers, body);
			res = (Comment)UtilLib.fromJsonToJava(serverres, Comment.class);
			cid = res.getName();
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (cid == null){
			return new StandardResponse(false, "Failed to communicate with firebase");
		}
		
		return new StandardResponse(true);
		
	}

	@Override
	public Settings getUserSettings(String token, String v) {
		// TODO Auto-generated method stub
		try {
			User user = userRepository.findbyToken(token);
			userRepository.updateVersion(token, v);
			Word version = wordRepository.getWordByKey("version");
			Word newUrl = wordRepository.getWordByKey("new_url");
			Settings setting = new Settings(user);
			if (newUrl.getEnglish().toLowerCase().equals("true")){
				setting.setElbUrl(true);
			} else {
				setting.setElbUrl(false);
			}
			int res = UtilLib.compareVersions(version.getEnglish(), v);
			if (res == 1){
				setting.setUpdateRequired(true);
			} else{
				setting.setUpdateRequired(false);
			}
			return setting;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse updateUserNumber(String token, String code) {
		// TODO Auto-generated method stub
		String authUrl = AppConfig.fbAuthTokenUrl + "?grant_type=authorization_code&"
				+ "code=" + code + "&access_token=AA|" + AppConfig.fbAppId + "|" + 
				AppConfig.fbAppSecret;
		FbAuthToken authRes = null;
		FbAuthVerification fbres = null;
		try {
			String res = http.sendGet(authUrl);
			authRes = (FbAuthToken) UtilLib.fromJsonToJava(res, FbAuthToken.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (authRes == null){
			return new StandardResponse(false, "Failed to retrieve userinfo from fb");
		}
		//return new StandardResponse(true, authRes.getAccess_token());
		try {
			String appsecret_proof = UtilLib.HmacSHA256(authRes.getAccess_token(), 
					AppConfig.fbAppSecret.getBytes("UTF8"));
			String verifyUrl = AppConfig.fbOTPVerificationUrl + "?appsecret_proof=" + 
					appsecret_proof + "&access_token=" + authRes.getAccess_token();
			String res = http.sendGet(verifyUrl);
			fbres = (FbAuthVerification) UtilLib.fromJsonToJava(res, FbAuthVerification.class);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (fbres == null){
			return null;
		}
		String number = fbres.getPhone().getNational_number();
		try {
			User u = userRepository.findByNumber(number);
			if (u != null){
				return new StandardResponse(false, "Number already exists in db");
			}
			User user = userRepository.findbyToken(token);
			user.setNumber(number);
			userRepository.update(user.getUserId(),
					new ObjectMapper().convertValue(new UserRequest(user), Map.class));
			return new StandardResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			return new StandardResponse(false, e.toString());
		}
		
	}

	@Override
	public List<Message> getRecentChats(String token, int limit, int page) {
		// TODO Auto-generated method stub
		List<Message> chats = new ArrayList<Message>();
		for (Map<String, Object> m: userRepository.getRecentChats(token, limit, page*limit)){
			System.out.println("=========>");
			System.out.println(m.keySet());
			System.out.println(m.values());
			chats.add(new ObjectMapper().convertValue(m, Message.class));
	
		}
		return chats;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse updateLan(String token, String lan, long type) {
		// TODO Auto-generated method stub
		try {
			User user = userRepository.findbyToken(token);
			if (user == null){
				return new StandardResponse(false, "No user found");
			}
			if (lan != null){
				user.setLan(lan);
			}
			if (type == 1){
				if(lan.equals("hinglish") || lan.equals("english")){
					user.setCommunityLan("hindi");
				}else {
					user.setCommunityLan(lan);
				}
			}
			userRepository.update(user.getUserId(), 
					new ObjectMapper().convertValue(new UserRequest(user), Map.class));
			return new StandardResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse updateNotif(String token, Settings setting) {
		// TODO Auto-generated method stub
		User user =null;
		try {
			user =  userRepository.findbyToken(token);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, "Failed to fetch user");
		}
		try {
			user.setNotif(!setting.isNotif());
			user.setNotifChat(!setting.isNotifChat());
			user.setNotifComment(!setting.isNotifComment());
			user.setNotifFollow(!setting.isNotifFollow());
			user.setNotifLike(!setting.isNotifLike());
			user.setNotifViralPosts(!setting.isNotifViralPosts());
			userRepository.update(user.getUserId(), new ObjectMapper().convertValue(
					new UserRequest(user), Map.class));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, "failed to update settings");
		}
		return new StandardResponse(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse updateContentLan(String token, Settings setting) {
		// TODO Auto-generated method stub
		if (setting.getContentLan() == null){
			return new StandardResponse(false, "contentLan cannot be null");
		} else if (setting.getContentLan().size() == 0){
			return new StandardResponse(false, "Atleast 1 language is needed");
		}
		try {
			User u = userRepository.findbyToken(token);
			u.setContentLan(setting.getContentLan());
			for (int i =0 ;i< u.getContentLan().size(); i++){
				u.getContentLan().set(i, u.getContentLan().get(i).toLowerCase());
			}
			userRepository.update(u.getUserId(), new ObjectMapper().convertValue(
					new UserRequest(u), Map.class));
			System.out.println(u.getContentLan());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, e.toString());
		}
		return new StandardResponse(true);
	}

	@Override
	public StandardResponse updatePhoneInfo(UserPhoneInfo info) {
		// TODO Auto-generated method stub
		try {
			upiRepository.save(info);
		} catch (Exception e) {
			// TODO: handle exception
			return new StandardResponse(false, e.toString());
		}
		return new StandardResponse(true, "saved successfully");
	}


	

}
