package com.wibe.backend.services;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.wibe.backend.entities.models.Asset;
import com.wibe.backend.entities.models.Banner;
import com.wibe.backend.responses.StandardResponse;

public interface CreationService {
	
	public List<Asset> getAssetsBytType(String type);
	
	public StandardResponse uploadAsset(MultipartFile file, MultipartFile thumbnails, 
			String type, String name, boolean live, int pos);
	
	public StandardResponse updateAsset(long assetId, int pos, boolean active);
	
	public Banner getBanner(String lan);
	
	public StandardResponse uploadBanner(MultipartFile file, String lan, String tag, int  duration, int height,
			int width, boolean live);
	
	public StandardResponse updateBanner(String lan, boolean active);
	
	public List<String> getUpTags();

}
