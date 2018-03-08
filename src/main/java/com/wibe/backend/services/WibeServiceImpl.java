package com.wibe.backend.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
//import java.util.Collections;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wibe.backend.config.AppConfig;
import com.wibe.backend.dto.CategoryInfoDTO;
import com.wibe.backend.dto.TagInfoDTO;
import com.wibe.backend.dto.WibeDTO;
//import com.amazonaws.services.s3.model.PutObjectResult;
import com.wibe.backend.dto.WibeInfoDTO;
import com.wibe.backend.entities.QueryResults.WibeObj;
import com.wibe.backend.entities.QueryResults.WibeObjIds;
import com.wibe.backend.entities.models.Category;
import com.wibe.backend.entities.models.Counter;
import com.wibe.backend.entities.models.Slot;
import com.wibe.backend.entities.models.Tag;
import com.wibe.backend.entities.models.User;
import com.wibe.backend.entities.models.Wibe;
import com.wibe.backend.entities.models.WibeList;
import com.wibe.backend.library.Notifications;
import com.wibe.backend.library.WibeLib;
import com.wibe.backend.repositories.CategoryRepository;
import com.wibe.backend.repositories.CounterRepository;
import com.wibe.backend.repositories.TagRepository;
import com.wibe.backend.repositories.UserRepository;
import com.wibe.backend.repositories.WibeListRepository;
import com.wibe.backend.repositories.WibeRepository;
import com.wibe.backend.repositories.WordRepository;
import com.wibe.backend.requests.Order;
import com.wibe.backend.requests.UserRequest;
import com.wibe.backend.requests.WibeRequest;
import com.wibe.backend.responses.StandardResponse;
import com.wibe.backend.responses.TrackInfo;


@Service
public final class WibeServiceImpl implements WibeService {
	
	private final UserRepository userRepository;
	private final CounterRepository counterRepository;
	private final TagRepository tagRepository;
	private final CategoryRepository categoryRepository;
	private final WibeRepository wibeRepository;
	private final WordRepository wordRepository;
	private final WibeListRepository wibeListRepository;
	private final WibeLib wibelib = WibeLib.getInstance();
	
	public WibeServiceImpl(UserRepository userRepository, 
			WibeRepository wibeRepository,
			TagRepository tagRepository,
			CategoryRepository categoryRepository,
			CounterRepository counterRepository,
			WordRepository wordRepository,
			WibeListRepository wibeListRepository) {
		this.wibeRepository = wibeRepository;
		this.userRepository = userRepository;
		this.tagRepository = tagRepository;
		this.categoryRepository = categoryRepository;
		this.counterRepository = counterRepository;
		this.wordRepository = wordRepository;
		this.wibeListRepository = wibeListRepository;
	}

	@SuppressWarnings("unchecked")
	@Override
	public WibeInfoDTO uploadWibe(MultipartFile file, Long uid, String desc, String music, 
			String artist, String cat, String t, String ext, double lat, double lon,
			int height, int width, boolean isPrivate) {
		// TODO Auto-generated method stub
		User user = userRepository.findByUserId(uid);
		if (user == null)
		{
			return null;
		}
		String fileType = file.getContentType();
		if (!fileType.startsWith("video")){
			return null;
		}
		String key = wibelib.getKey(counterRepository, "wibeId");
		String bucket = "wibessin";
		String[] keys = key.split("/");
		Long wibeId = Long.parseLong(keys[keys.length-1]);
		String thumbnail = AppConfig.thumbnailDomain + key + "/" 
							+ Long.toString(wibeId) + "-00001.jpg";
		String url = AppConfig.wibeDomain + key + "/" + 
							Long.toString(wibeId) + ".m3u8";
		key = key + "." + ext;
		TrackInfo track = new TrackInfo(music, artist);
		List<String> categories, tags;
		try {
			categories = Arrays.asList(cat.split(","));
			
		} catch (Exception e) {
			// TODO: handle exception
			categories = new ArrayList<String>();
		}		
		if (t == null || t.equals("")){
			tags = new ArrayList<String>();
		} else {
			try {
				tags = Arrays.asList(t.split(","));
				System.out.println(tags);
				for (String tag : tags){
					tag = tag.trim().toLowerCase();
					Tag obj = tagRepository.findTagByName(tag);
					if (obj == null){
						tagRepository.create(wibelib.getNextCount(counterRepository, "tagId"),
								tag);
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				tags = new ArrayList<String>();
			}
		}
		
		Wibe wibe = new Wibe(wibeId, uid, desc, url, categories, tags, 
				track, DateUtils.addMinutes(new Date(), 5), ext, lat, lon, thumbnail, height, width, isPrivate);
		wibe.setLanguage(user.getCommunityLan());
		wibe.setLockedTill(wibe.getUploadedAt().getTime());
		try {
			InputStream is = file.getInputStream();
			//PutObjectResult response = wibelib.uploadToS3(bucket, key, is, 
			//		new ObjectMetadata());
			wibelib.uploadToS3(bucket, key, is, new ObjectMetadata());
			wibeRepository.update(wibe.getWibeId(), new ObjectMapper().convertValue(
					new WibeRequest(wibe), Map.class));
			try{
				Wibe node = wibeRepository.addWibeToLayer(wibe.getWibeId());
				System.out.println("======>node:");
				System.out.print(node);
			}catch (Exception e){
				e.printStackTrace();
			}
//			try {
//				Runnable updateLatLon = new Runnable(){
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						wibeRepository.addWibesToLayer();
//						
//					}
//				};
//				new Thread(updateLatLon).run();
//			} catch (Exception e) {
//				// TODO: handle exception
//				e.printStackTrace();
//			}
			wibeRepository.updateUploader(wibe.getWibeId(), wibe.getUploaderId());
			for (String tag : tags){
				tag = tag.trim().toLowerCase();
				tagRepository.tagVideo(wibe.getWibeId(), tag);
			}
			for (String c : categories){
				categoryRepository.catVideo(wibe.getWibeId(), c);
			}
		} catch(IOException e){
			e.printStackTrace();
			return null;
		}
		return new WibeInfoDTO(wibe);
	}

	@Override
	public void previewWibe(HttpServletResponse response, Long wid) {
		// TODO Auto-generated method stub
		Wibe wibe = wibeRepository.findByWibeId(wid);
		String url = wibe.getUrl();
		String[] folders = url.split("/");
		String bucket = folders[0];
		folders[0] = "";
		String key = url.substring(url.indexOf("/") + 1,url.length());
		try {
			InputStream iStream = wibelib.streamFroms3(bucket, key);
			response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
	        response.setHeader("Content-Disposition", "attachment; filename="+ Long.toString(wibe.getWibeId()) +"." +  wibe.getExt());
	        IOUtils.copy(iStream, response.getOutputStream());
	        response.flushBuffer();
	    } catch (java.nio.file.NoSuchFileException e) {
	        response.setStatus(HttpStatus.NOT_FOUND.value());
	    } catch (Exception e) {
	        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WibeDTO> getWibes(String type, long id, long page, long limit,
			double lat, double lon, double radius, String token, String lan, long zeroTime, long seed) {
		// TODO Auto-generated method stub
		boolean isZeroTime = true;
		if (zeroTime == 0) {
			zeroTime = new Date().getTime();
			isZeroTime = false;
		}
		User user = null;
		try {
			if (token !=null)
				user = userRepository.findbyToken(token);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		if (page == 0){
			try {
				if (user != null && !isZeroTime){
					user.setZeroTime(System.currentTimeMillis());
					user.setSeed(new Random().nextLong());
					userRepository.update(user.getUserId(), new ObjectMapper().convertValue(
						new UserRequest(user), Map.class));
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		
		List<WibeDTO> wibedto = new ArrayList<WibeDTO>();
		List<WibeObj> wibes = null;
		if (type.equals("nearby")){
			type= "explore";
		}
		if (user != null){
			System.out.println(user);
			if (user.getCommunityLan().equals("gujarati") && type.equals("general")){
				type = "explore";
			}
			if (user.getCommunityLan().equals("tamil") && type.equals("general")){
				type = "created";
			}
		}
		if (token ==null) {
			if (lan.equals("gujarati") && type.equals("general")){
				type = "explore";
			}
			if (lan.equals("tamil") && type.equals("general")){
				type = "created";
			}
		}
		try {
			if (type.equals("general")){
				Counter counter = counterRepository.findByCounterId("wibeId");
				if (token != null) {
					if (!isZeroTime) {
						wibes = wibeRepository.getListWibes2(page * limit, limit, counter.getSeq(), 
								user.getZeroTime(), user.getContentLan());
					} else {
						wibes = wibeRepository.getListWibes2(page * limit, limit, counter.getSeq(), 
								zeroTime, user.getContentLan());
					}
					List<Long> blockedUsers = userRepository.getBlockedUsersId(token);
					for (WibeObj w: wibes){
						for (Long num : blockedUsers){
							if (w.getUser().getUserId() == num.longValue()){
								wibes.remove(w);
							}
						}
					}
				} else {
					List<String> lans = new ArrayList<>();
					lans.add(lan);
					if (!lan.equals("hindi"))
						lans.add("hindi");
					wibes = wibeRepository.getListWibes2(page * limit, limit, counter.getSeq(), 
							zeroTime, lans);
				}
				if (token !=null) {
					List<Slot> slots = wibeRepository.getSlots();
					for (Slot s : slots){
						if (page == s.getPage()){
							if (wibes.size() > s.getPos()-1){
								if (wibeRepository.verifyView(s.getWid(), user.getUserId()) ==0){
									WibeObj w = wibeRepository.getOne(s.getWid());
									if (! w.getWibe().isDeleted()){
										if (s.getViewLimit() == 0){
											wibes.add(s.getPos(), w);
										} else if (s.getViewLimit() > w.getWibe().getNumViews()){
											wibes.add(s.getPos(), w);
										}
									}
								}
							}
						}
					}
				}
			} else if (type.equals("nearby")){
				if (!isZeroTime) {
					wibes = wibeRepository.getNearbyWibes(page * limit, limit, lat, lon, radius,
							token, user.getZeroTime());
				} else {
					wibes = wibeRepository.getNearbyWibes(page * limit, limit, lat, lon, radius,
							token, zeroTime);
				}
			} else if (type.equals("foryou")){
				if (!isZeroTime) {
					wibes = wibeRepository.getPersonalWibes(page * limit, limit, id, 
							user.getZeroTime());
				} else {
					wibes = wibeRepository.getPersonalWibes(page * limit, limit, id, 
							zeroTime);
				}
			}else if (type.equals("favourite")){
				wibes = wibeRepository.getFavourites(id, page * limit, limit);
			} else if (type.equals("personal")){
				if (token != null) {
					if (user.getToken().equals(token)){
						wibes = wibeRepository.getMyUploads(id, page * limit , limit);
					} else {
						wibes = wibeRepository.getUploads(id, page * limit, limit, token);
					}
				}else {
					wibes = wibeRepository.getMyUploads(id, page * limit , limit);
				}
			} else if (type.equals("trending")){
				Counter counter = counterRepository.findByCounterId("wibeId");
				if (token != null) {
					if (!isZeroTime) {
						wibes = wibeRepository.getTrendingWibes(page * limit, limit, token, 
								user.getZeroTime(), counter.getSeq());
					} else {
						wibes = wibeRepository.getTrendingWibes(page * limit, limit, token, 
								zeroTime, counter.getSeq());
					}
				}else {
					wibes = wibeRepository.getTrendingWibes(page * limit, limit, token, 
							zeroTime, counter.getSeq());
				}
			} else if (type.equals("explore") || type.equals("created")){
				wibes = new ArrayList<WibeObj>();
				if (token != null) {
					if (!isZeroTime) {
						seed = user.getSeed();
					}
				}
				Random rand = new Random(seed);
				Counter counter = counterRepository.findByCounterId("wibeId");
				long num = 20000;
				List<String> lans = new ArrayList<>();
				lans.add(lan);
				if (!lan.equals("hindi"))
					lans.add("hindi");
				if (token != null) {
					if (user.getContentLan().size() == 1){
						if (!user.getContentLan().contains("hindi")){
							num = 50000;
						}
					}
				} else {
					if (lans.size() == 1){
						if (lans.contains("hindi")){
							num = 50000;
						}
					}
				}
				List<WibeObjIds> wibes2 = null;
				if (type.equals("explore")) {
					if (token != null) {
						if (!isZeroTime) {
							wibes2 = wibeRepository.getRecentWibes2(counter.getSeq(), 
									user.getZeroTime(), user.getContentLan(), num);
						} else {
							wibes2 = wibeRepository.getRecentWibes2(counter.getSeq(), 
									zeroTime, user.getContentLan(), num);
						}
					} else {
						wibes2 = wibeRepository.getRecentWibes2(counter.getSeq(), 
								zeroTime, lans, num);
					}
				} else if (type.equals("created")) {
					if (token != null) {
						if (!isZeroTime) {
							wibes2 = wibeRepository.getCreatedWibes(user.getZeroTime());
						} else {
							wibes2 = wibeRepository.getCreatedWibes(zeroTime);
						}
					} else {
						wibes2 = wibeRepository.getCreatedWibes(zeroTime);
					}
				}
				List<Long> blockedUsers = new ArrayList<>();
				if (token !=null) {
					blockedUsers = userRepository.getBlockedUsersId(token);
				}
				List<Order> order = new ArrayList<>();
				boolean flag = false;
                for (int i=0; i<wibes2.size(); i++){
                		if (token!=null) {
                			for (Long n : blockedUsers){
                				if (n.longValue() == wibes2.get(i).getUploaderId()){
                					flag = true;
                					break;
                				}
                			}
                		}
                    if (!flag){
                    	order.add(new Order(wibes2.get(i).getWibeId(), rand.nextInt(5000)));
                    }
                    flag = false;
                }
				order.sort(new Comparator<Order>() {
					@Override
					public int compare(Order o1, Order o2) {
						// TODO Auto-generated method stub
						if (o1.getOrder() == o2.getOrder()){
							return 0;
						} else if (o1.getOrder() > o2.getOrder() ){
							return 1;
						} else{
							return -1;
						}
					}
				});
				List<Long> wibes1 = new ArrayList<>();
				int start = 0, end =0;
				if (page *limit >= order.size()){
					start = end =0;
				} else if ((page * limit) + limit > order.size()){
					start = (int) (page * limit);
					end = order.size();
				} else {
					start = (int)(page * limit);
					end = (int)((page * limit) + limit);
				}
				for (int i= start; i< end; i++){
					wibes1.add(order.get(i).getNum());
				}
				wibes = wibeRepository.getWibesList(wibes1);
			}  else if (type.equals("pending")){
				wibes = wibeRepository.getPendingWibes();
			}
			if (wibes == null){
				return null;
			}
//			wibeinfodto = new ArrayList<WibeInfoDTO>(Collections.nCopies(wibes.size(), null));
//			wibelib.createWibeDTO(wibes, wibeRepository, userRepository, wibeinfodto, id,
//					true, true, true);
			wibedto = new ArrayList<WibeDTO>();
			for (WibeObj w: wibes){
				wibedto.add(new WibeDTO(w));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return wibedto;
	}

	@Override
	public TagInfoDTO getTagInfo(String name) {
		// TODO Auto-generated method stub
		Tag tag = tagRepository.findTagByName(name);
		TagInfoDTO tagdto = null;
		if (tag != null){
			tagdto = new TagInfoDTO(tag,"english");
			long count = tagRepository.getNumWibesTag(name);
			if (count < 400){
				tagdto.setNumWibes(tagRepository.getNumWibesRel(name));
			} else {
				tagdto.setNumWibes(count);
			}
		}
		return tagdto;
	}

	@Override
	public CategoryInfoDTO getCategoryInfo(String name, long uid, String lan) {
		// TODO Auto-generated method stub
		Category cat = categoryRepository.getByCatName(name);
		CategoryInfoDTO catdto = new CategoryInfoDTO(cat, lan, categoryRepository);
		catdto.setFollowing(categoryRepository.verifyFollower(uid, name) != 0);
		return catdto;
	}

	@Override
	public WibeInfoDTO getWibe(long wid, long uid) {
		// TODO Auto-generated method stub
		Wibe wibe = wibeRepository.findByWibeId(wid);
		if (wibe == null){
			return null;
		}
		User user = userRepository.findByUserId(wibe.getUploaderId());
		WibeInfoDTO wibedto = new WibeInfoDTO(wibe);
		wibedto.setCommentsRestricted(user.isCommentsRestricted() ||
				wibe.isCommentsRestricted());
		wibelib.addCounts(wibedto, wibeRepository, wid, uid, false, wibe);
		if(user.getUsername() != null)
			wibedto.setUploaderName(user.getUsername());
		if(user.getImgUrl() != null)
			wibedto.setUploaderPicUrl(user.getImgUrl());
		Runnable updatebbox = new Runnable(){

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				// TODO Auto-generated method stub
				wibe.setBbox(new double[]{wibe.getLongitude(), wibe.getLatitude(),
						wibe.getLongitude(), wibe.getLatitude()});
				wibeRepository.update(wibe.getWibeId(), new ObjectMapper().convertValue(
						new WibeRequest(wibe), Map.class));
				wibeRepository.addWibeToLayer(wibe.getWibeId());
			}
			
		};
		if (wibe.getBbox() == null || wibe.getBbox().length == 0){
			new Thread(updatebbox).start();
		}
		return wibedto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse restrictComments(long wid, boolean val, String token) {
		// TODO Auto-generated method stub
		try {
			Wibe wibe = wibeRepository.getWibeByIdAndToken(wid, token);
			if (wibe == null){
				return new StandardResponse(false, "Wibe doesnt exist or "
						+ "you are not the author of the video");
			}
			wibe.setCommentsRestricted(val);
			Map<String, Object> map = new ObjectMapper().convertValue(
							new WibeRequest(wibe), Map.class);
			System.out.println(map);
			wibeRepository.update(wibe.getWibeId(), new ObjectMapper().convertValue(
					new WibeRequest(wibe), Map.class));
			return new StandardResponse(true);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, e.toString());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse deleteVideo(long wid, String token) {
		// TODO Auto-generated method stub
		try {
			Wibe wibe = wibeRepository.getWibeByIdAndToken(wid, token);
			if (wibe == null){
				return new StandardResponse(false, "Wibe doesnt exist or "
						+ "you are not the author of the video");
			}
			wibe.setDeleted(true);
			if (!wibe.isApproved()){
				wibe.setApproved(true);
			}
			wibeRepository.update(wibe.getWibeId(), new ObjectMapper().convertValue(
					new WibeRequest(wibe), Map.class));
			return new StandardResponse(true);
		} catch (Exception e) {
			return new StandardResponse(false, e.toString());
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public StandardResponse increaseWhatsAppShare(String token, long wid) {
		// TODO Auto-generated method stub
		Wibe wibe = null;
		try {
			wibe = wibeRepository.findByWibeId(wid);
			wibe.setWhatsAppShare(wibe.getWhatsAppShare() + 1);
			wibeRepository.update(wid,
					new ObjectMapper().convertValue(new WibeRequest(wibe), Map.class));
		} catch (Exception e) {
			// TODO: handle exception
			return new StandardResponse(false, e.toString());
		}
		try {
			userRepository.updateWShareCount(token, wid);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if ( wibe != null)
			return new StandardResponse(true, wibe.getWhatsAppShare());
		else
			return new StandardResponse(false);
	
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public StandardResponse increaseShareCount(String token, long wid) {
		// TODO Auto-generated method stub
		Wibe wibe = null;
		try {
			wibe = wibeRepository.findByWibeId(wid);
			wibe.setShareCount(wibe.getShareCount() + 1);
			wibeRepository.update(wid,
					new ObjectMapper().convertValue(new WibeRequest(wibe), Map.class));
			
		} catch (Exception e) {
			// TODO: handle exception
			return new StandardResponse(false, e.toString());
		}
		try {
			userRepository.updateOShareCount(token, wid);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if ( wibe != null)
			return new StandardResponse(true, wibe.getWhatsAppShare());
		else
			return new StandardResponse(false);
	}
	
	@SuppressWarnings({ "unchecked", "unused" })
	@Override
	public StandardResponse increaseDownloadCount(String token, long wid) {
		// TODO Auto-generated method stub
		Wibe wibe =null;
		try {
			wibe = wibeRepository.findByWibeId(wid);
			wibe.setDownloadCount(wibe.getDownloadCount() + 1);
			wibeRepository.update(wid,
					new ObjectMapper().convertValue(new WibeRequest(wibe), Map.class));
			
		} catch (Exception e) {
			// TODO: handle exception
			return new StandardResponse(false, e.toString());
		}
		try {
			userRepository.updateDownloadCount(token, wid);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if ( wibe != null)
			return new StandardResponse(true, wibe.getDownloadCount());
		else
			return new StandardResponse(false);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse deleteWibe(long wid, boolean status, boolean censor, String t) {
		// TODO Auto-generated method stub
		try {
			Wibe wibe = wibeRepository.getByWibeId(wid);
			wibe.setApproved(true);
			wibe.setApprovedAt(new Date().getTime());
			wibe.setDeleted(status);
			wibeRepository.update(wibe.getWibeId(), new ObjectMapper().convertValue(
					new WibeRequest(wibe), Map.class));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, e.toString());
		}
		List<String> tags= new ArrayList<String>();
		try {
			if (!tags.equals("")){
				tags = Arrays.asList(t.split(","));
			}
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		if (status){
			Notifications.notifyDelete(wid, userRepository, wibeRepository, wordRepository);
		} else {
			if (censor){
				tags.add("18+");
			}
			for (String tag : tags){
				if (tag.equals("")){
					continue;
				}
				Tag obj = tagRepository.findTagByName(tag);
				if (obj == null){
					tagRepository.create(wibelib.getNextCount(counterRepository, "tagId"),
							tag);
				}
				tagRepository.tagVideo(wid, tag);
			}
		}
		return new StandardResponse(true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse updateList(WibeList list) {
		// TODO Auto-generated method stub
		try {
			WibeList wibeList = wibeListRepository.findByListId(list.getListId());
			wibeList.setWibes(list.getWibes());
			wibeListRepository.update(wibeList.getListId(), new ObjectMapper().convertValue(wibeList, 
					Map.class));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, e.toString());
		}
		
		return new StandardResponse(true);
	}
}
