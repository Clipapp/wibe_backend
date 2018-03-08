package com.wibe.backend.library;

import java.util.Random;

public class AlphaNumericSystem {
	
	final protected static String alphanumericString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	final protected static char[] alphanumericArray = alphanumericString.toCharArray();
	
	public static String numToChar(long num){
		int len = 0;
		long tmp = num;
		while (tmp > 0){
			tmp = tmp /62;
			len++;
		}
		if (len==0){
			len=1;
		}
		char[] arr = new char[len];
		while (len > 0){
			len--;
			arr[len] = alphanumericArray[(int) (num % 62)];
			num = num/62;
		}
		return new String(arr);
	}
	
	public static long charToNum (String str){
		long num = 0;
		int len = str.length();
		for (int i = 0 ; i < len; i++){
			num += Math.pow(62, i) * alphanumericString.indexOf(str.charAt(len -1 -i));
		}
		return num;
	}
	
	public static void main(String[] args){
		//System.out.println(numToChar(1222234567890L));
		
		//Random r = new Random();
		for (int i=0 ;i<10;i++){
			Random rand  = new Random(12);
			System.out.println(rand.nextInt(5000));
			//System.out.println(r.nextInt(5000));
		}
	}
}
