package com.wibe.backend.library;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.IOUtils;

//import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.wibe.backend.dto.WibeInfoDTO;
import com.wibe.backend.entities.models.Counter;
import com.wibe.backend.entities.models.User;
import com.wibe.backend.entities.models.Wibe;
import com.wibe.backend.repositories.CounterRepository;
import com.wibe.backend.repositories.UserRepository;
import com.wibe.backend.repositories.WibeRepository;
import com.wibe.backend.threads.WibeDTOUpdateThread;

public final class WibeLib {
	
	private static WibeLib wibelib;
	
	private WibeLib(){
		
	}
	
	public static WibeLib getInstance(){
		if (wibelib == null){
			wibelib = new WibeLib();
		}
		return wibelib;
	}
	
	public Long getNextCount(CounterRepository counterRepository, String name){
        Counter obj = counterRepository.findAndIncreaseByCounterId(name);
        return obj.getSeq();
	}
	
	public PutObjectResult uploadToS3(String bucket, String key, InputStream is, ObjectMetadata meta) throws IOException{
		byte[] bytes = IOUtils.toByteArray(is);
		meta.setContentLength(bytes.length);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, byteArrayInputStream, meta);
		AmazonS3 s3Client = new AmazonS3Client();
		PutObjectResult res = s3Client.putObject(putObjectRequest);
		return res;
	}
	
	public InputStream streamFroms3(String bucket, String key){
		AmazonS3 s3Client = new AmazonS3Client();
		S3Object obj = s3Client.getObject(bucket, key);
		return obj.getObjectContent();
	}
	
	public String getKey(CounterRepository counterRepository,String name){
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		String year  = Integer.toString(localDate.getYear());
		String month = Integer.toString(localDate.getMonthValue());
		String day   = Integer.toString(localDate.getDayOfMonth());
		Long id = this.getNextCount(counterRepository, name);
		String key = year + "/" + month + "/" + day + "/" + id; 
		return  key;
	}
	
	public void addCounts(WibeInfoDTO wibe, WibeRepository repo , long wid, long uid,
			boolean onlyViews, Wibe w){
		if (onlyViews){
			wibe.setNumViews(w.getNumViews());
		}else{
			wibe.setNumComments(repo.getCommentCount(wid));
			wibe.setNumFavourites(repo.getFavouriteCount(wid));
			wibe.setNumLikes(repo.getLikeCount(wid));
			wibe.setNumShares(repo.getShareCount(wid));
			//wibe.setNumViews(repo.getViewCount(wid));
			wibe.setNumViews(w.getNumViews());
			wibe.setLiked(repo.verifyLiked(wid, uid) != 0);
			wibe.setFavourite(repo.verifyFavourite(wid, uid) != 0);
			wibe.setFollowing(repo.verifyFollowing(wid, uid) != 0);
		}
	}
	
	public void addUserInfo(WibeInfoDTO wibe, UserRepository userRepo, long uid){
		User user = userRepo.findByUserId(uid);
		if (user!=null){
			if (user.getUsername() != null)
				wibe.setUploaderName(user.getUsername());
			if (user.getImgUrl() != null)
				wibe.setUploaderPicUrl(user.getImgUrl());
		}
	}
	
	public void createWibeDTO(List<Wibe> wibes, WibeRepository wibeRepo, 
			UserRepository userRepo, List<WibeInfoDTO> wibedto, long id, boolean addCounts,
			boolean addUserInfo, boolean onlyViews){
		ExecutorService executor = Executors.newFixedThreadPool(25);
		for (int i =0; i< wibes.size(); i++){
			Runnable worker = new WibeDTOUpdateThread(wibedto, wibes.get(i), wibeRepo, 
					userRepo, id, i, addCounts, addUserInfo, onlyViews);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()){
			
		}
		
	}
	
	
}
