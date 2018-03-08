package com.wibe.backend.library;

//import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wibe.backend.entities.models.Counter;
import com.wibe.backend.repositories.CounterRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class UtilLib {
	
	private static UtilLib utilLib;
	
	final protected static char[] hexArray = "0123456789abcdef".toCharArray();
	
	private UtilLib(){
		
	}
	
	public static UtilLib getInstance(){
		if (utilLib == null){
			utilLib = new UtilLib();
		}
		return utilLib;
	}
	
	/**
     * Convert a JSON string to an object
     * @param json
     * @return
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     */
     @SuppressWarnings("unchecked")
     public static Object fromJsonToJava(String json, @SuppressWarnings("rawtypes") Class type) throws JsonParseException,
                     JsonMappingException, IOException {
          ObjectMapper jsonMapper = new ObjectMapper();
          return jsonMapper.readValue(json, type);
     }
     
     /**
      * Get the unique id of the object
      * @param counterRepository
      * @param name
      * @return
      */
     public static long getNextCount(CounterRepository counterRepository, String name){
         Counter obj = counterRepository.findAndIncreaseByCounterId(name);
         return obj.getSeq();
 	}
     
    /**
     * Get the key of the object to be uploaded in S3 Bucket
     * @param counterRepository
     * @param name
     * @return
     */
 	public static String getKey(CounterRepository counterRepository, String name){
		Date date = new Date();
		LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		String year  = Integer.toString(localDate.getYear());
		String month = Integer.toString(localDate.getMonthValue());
		String day   = Integer.toString(localDate.getDayOfMonth());
		long id = getNextCount(counterRepository, name);
		String key = year + "/" + month + "/" + day + "/" + id; 
		return  key;
	}
 	
 	public static PutObjectResult uploadToS3(String bucket, String key, InputStream is, ObjectMetadata meta){
		AmazonS3 s3Client = new AmazonS3Client();
		PutObjectResult res = s3Client.putObject(bucket, key, is, meta);
		return res;
	}
	
	public InputStream streamFroms3(String bucket, String key){
		AmazonS3 s3Client = new AmazonS3Client();
		S3Object obj = s3Client.getObject(bucket, key);
		return obj.getObjectContent();
	}
	
	public static String HmacSHA256(String data, byte[] key) throws Exception {
	    String algorithm="HmacSHA256";
	    Mac mac = Mac.getInstance(algorithm);
	    mac.init(new SecretKeySpec(key, algorithm));
	    return bytesToHex(mac.doFinal(data.getBytes("UTF8")));
	}
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static byte[] hexToBytes(char[] hex)
	  {
	    byte[] raw = new byte[hex.length / 2];
	    for (int src = 0, dst = 0; dst < raw.length; ++dst) {
	      int hi = Character.digit(hex[src++], 16);
	      int lo = Character.digit(hex[src++], 16);
	      if ((hi < 0) || (lo < 0))
	        throw new IllegalArgumentException();
	      raw[dst] = (byte) (hi << 4 | lo);
	    }
	    return raw;
	  }
	
	public static String HmacSHA1(String data, byte[] key) throws Exception {
		String algorithm ="HmacSHA1";
		Mac mac = Mac.getInstance(algorithm);
		mac.init(new SecretKeySpec(key, algorithm));
		return bytesToHex(mac.doFinal(data.getBytes("UTF8")));
	}
	
	public static List<Integer> getRandomList(int seed, long num){
		List<Integer> nums = new ArrayList<Integer>();
		Random randomGenerator = new Random(seed);
		for (long i=0; i<num; i++){
			nums.add(randomGenerator.nextInt(10000000));
		}
		return nums;
	}
	
	public static int compareVersions(String v1, String v2){
		String[] t1 = v1.split("\\.");
		String[] t2 = v2.split("\\.");
		if (t1.length != t2.length){
			return -2;
		}
		return recursiveCompare(t1, t2, 0);
	}
	
	private static int recursiveCompare(String[] t1, String[] t2, int pos){
		if (pos == t1.length){
			return 0;
		}
		int n1,n2;
		try {
			n1 = Integer.parseInt(t1[pos]);
			n2 =  Integer.parseInt(t2[pos]);
		} catch (Exception e) {
			// TODO: handle exception
			return -3;
		}
		if (n1 > n2){
			return 1;
		} else if (n1 < n2){
			return -1;
		} else {
			return recursiveCompare(t1, t2, pos+1);
		}
	}


}
