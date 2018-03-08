package com.wibe.backend.library;

//import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

//import com.wibe.backend.config.AppConfig;

//import com.wibe.backend.config.AppConfig;

public class Encryptor {
    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());

            return UtilLib.bytesToHex(encrypted);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(UtilLib.hexToBytes(encrypted.toCharArray()));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
    
    public static String encode(String key, String data) throws Exception {
    	  Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    	  SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
    	  sha256_HMAC.init(secret_key);

    	  return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
    	}
    
    public static void main(String [] args) throws Exception {
    	  System.out.println(encode("Random Shit", "+917892356935, 1515285486000"));
    	}
    
//    public static void main(String[] args){
//    	System.out.println(encrypt(AppConfig.messageKeySecret,AppConfig.messageIVector,"111308127786"));
//    }

    
}
