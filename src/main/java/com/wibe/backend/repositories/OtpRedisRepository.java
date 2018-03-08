package com.wibe.backend.repositories;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.wibe.backend.entities.models.Otp;

@RepositoryRestResource(exported=false)
@Repository
public class OtpRedisRepository {
	
	@Inject
	private RedisTemplate<String, Otp> redisTemplate;
	
	public void save(Otp otp) {
		redisTemplate.opsForValue().set(otp.getNumber(), otp, 10, TimeUnit.MINUTES);
	}
 
	public Otp findByNumber(String key) {
		return redisTemplate.opsForValue().get(key);
	}
	
	public List<Otp> findAll() {
		List<Otp> otps = new ArrayList<>();
		
		Set<String> keys = redisTemplate.keys("*");
		Iterator<String> it = keys.iterator();
		
		while(it.hasNext()){
			otps.add(findByNumber(it.next()));
		}
		
		return otps;
	}
	
	public void delete(Otp otp) {
		String key = otp.getNumber();
		redisTemplate.opsForValue().getOperations().delete(key);
	}
	
	 
	public void deleteAll() {
		Set<String> keys = redisTemplate.keys("*");
		Iterator<String> it = keys.iterator();
		
		while(it.hasNext()){
			redisTemplate.opsForValue().getOperations().delete(it.next());
		}
	}

}
