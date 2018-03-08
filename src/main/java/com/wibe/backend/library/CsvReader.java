package com.wibe.backend.library;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CsvReader {
	
	private static CsvReader countryMap;
	
	private HashMap<String, String> countryCodesMap;
	
	private CsvReader() {
		this.init();
	}
	
	public static CsvReader getInstance() {
		if (countryMap == null) {
			countryMap = new CsvReader();
		}
		return countryMap;
	}
	public void init() {
		countryCodesMap = new HashMap<>();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			InputStream is = classLoader.getResourceAsStream("CountryCodes.csv");
			int i = 0,countryCode=-1, phoneCode =-1;
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			for (String line; (line = br.readLine()) != null; ) {
				if (i == 0) {
					List<String> keys = Arrays.asList(line.toLowerCase().split(","));
					countryCode = keys.indexOf("country code");
					phoneCode = keys.indexOf("phone code");
				} else {
					String[] keys = line.toLowerCase().split(",", -1);
					if (countryCode !=-1 && phoneCode != -1) {
						countryCodesMap.put(keys[countryCode].trim(), keys[phoneCode].trim());
					}
				}
				i++;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getPhoneCode(String countryCode) {
		return countryCodesMap.get(countryCode.toLowerCase());
	}
	
	public static void main(String[] args) {
		CsvReader reader = CsvReader.getInstance();
		System.out.println(reader.getPhoneCode("in"));
	}
}

