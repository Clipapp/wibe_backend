package com.wibe.backend.services;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wibe.backend.config.AppConfig;
import com.wibe.backend.entities.models.Asset;
import com.wibe.backend.entities.models.Banner;
import com.wibe.backend.entities.models.UpTags;
import com.wibe.backend.library.UtilLib;
import com.wibe.backend.repositories.AssetRepository;
import com.wibe.backend.repositories.CounterRepository;
import com.wibe.backend.repositories.TagRepository;
import com.wibe.backend.responses.StandardResponse;

@Service
public class CreationServiceImpl implements CreationService{
	
	private final AssetRepository assetRepository;
	private final CounterRepository counterRepository;
	private final TagRepository tagRepository;
	
	public CreationServiceImpl(AssetRepository assetRepository, 
			CounterRepository counterRepository,
			TagRepository tagRepository){
		this.assetRepository = assetRepository;
		this.counterRepository = counterRepository;
		this.tagRepository = tagRepository;
	}

	@Override
	public List<Asset> getAssetsBytType(String type) {
		// TODO Auto-generated method stub
		return assetRepository.findbyAssetType(type);
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse uploadAsset(MultipartFile file, MultipartFile thumbnails, 
			String type, String name, boolean live, int pos) {
		// TODO Auto-generated method stub
		long id = UtilLib.getNextCount(counterRepository, "assetId");
		String  assetExt="", thumbExt = "";
		try {
			assetExt = file.getContentType().split("/")[1];
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			thumbExt= thumbnails.getContentType().split("/")[1];
		} catch (Exception e) {
			// TODO: handle exception
		}
		String keyAsset = "static/assets/" + type + "/" + Long.toString(id) + "/" 
				+ Long.toString(id) + "." + assetExt;
		String keyThumb = "static/assets/" + type + "/" + Long.toString(id) + "/" + 
				Long.toString(id) + "_thumb" + "." + thumbExt;
		String url_asset = AppConfig.imageThumbDomain + keyAsset;
		String url_thumb = AppConfig.imageThumbDomain + keyThumb;
		String bucket = "clipsterthumbs";
		try {
			InputStream is = file.getInputStream();
			UtilLib.uploadToS3(bucket, keyAsset, is, new ObjectMetadata());
			is = thumbnails.getInputStream();
			UtilLib.uploadToS3(bucket, keyThumb, is, new ObjectMetadata());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return new StandardResponse(false, e.toString());
		}
		Asset asset = new Asset(id, url_asset, url_thumb, type, name, live);
		asset.setUploadedAt(System.currentTimeMillis());
		try {
			List<Asset> assets = assetRepository.findbyAssetType(type);
			if (pos < 0)
				pos =0;
			if (pos >= assets.size()) {
				assetRepository.update(asset.getAssetId(), new ObjectMapper().convertValue(
						asset, Map.class));
			} else {
				assets.add(asset);
				for (int j = pos; j < assets.size() - 1; j++) {
					swapAssets(assets, j, assets.size() -1 );
				}
				for (Asset a:assets) {
					assetRepository.update(a.getAssetId(), new ObjectMapper().convertValue(
							a, Map.class));
				}
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, e.toString());
		}
		return new StandardResponse(true);
	}

	@Override
	public Banner getBanner(String lan) {
		// TODO Auto-generated method stub
		try {
			Banner b = tagRepository.getBanner(lan.toLowerCase());
			return b;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<String> getUpTags() {
		// TODO Auto-generated method stub
		try {
			UpTags t = tagRepository.getUpTag();
			return t.getTags();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public StandardResponse uploadBanner(MultipartFile file, String lan, String tag, int duration, int height,
			int width, boolean live) {
		//String  assetExt="";
		String name ="";
		try {
			//assetExt = file.getContentType().split("/")[1];
			name = file.getOriginalFilename();
		} catch (Exception e) {
			// TODO: handle exception
		}
		String keyAsset = "static/assets/banner/" + name;
		String url_asset = AppConfig.imageThumbDomain + keyAsset;
		String bucket = "clipsterthumbs";
		try {
			InputStream is = file.getInputStream();
			UtilLib.uploadToS3(bucket, keyAsset, is, new ObjectMetadata());
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
			return new StandardResponse(false, e.toString());
		}
		Banner banner = null;
		try {
			banner = assetRepository.updateBanner(lan, url_asset, tag, live, duration, height, width);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new StandardResponse(false, e.toString());
		}
		return new StandardResponse(true, "Banner Updated Succesfully", banner);
	}

	@Override
	public StandardResponse updateBanner(String lan, boolean active) {
		// TODO Auto-generated method stub
		try {
			Banner b = assetRepository.changeBannerStatus(lan, active);
			if (b == null)
				return new StandardResponse(false,"No banner with the given language found");
		} catch (Exception e) {
			// TODO: handle exception
			return new StandardResponse(false, e.toString());
		}
		return new StandardResponse(true, "Banner updated Succesfully");
	}

	@SuppressWarnings("unchecked")
	@Override
	public StandardResponse updateAsset(long assetId, int pos, boolean active) {
		// TODO Auto-generated method stub
		if (active == false) {
			try {
				Asset a = assetRepository.changeAssetStatus(assetId, active);
				if (a== null)
					return new StandardResponse(false, "No asset with given assetId found");
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return new StandardResponse(false, e.toString());
			}
		} else {
			try {
				Asset a = assetRepository.getAssetById(assetId);
				if (a==null) {
					return new StandardResponse(false, "No asset with given AssetId found");
				}
				List<Asset> assets = assetRepository.findbyAssetType(a.getAssetType());
				for (Asset ass: assets) {
					if (ass.getAssetId() == a.getAssetId()) {
						assets.remove(ass);
						break;
					}
				}
				if (assets.size() > 0)
					a.setWeight(assets.get(assets.size()-1).getWeight() + 100000);
				if (pos < 0)
					pos =0;
				if (pos >= assets.size()) {
					assetRepository.update(a.getAssetId(), new ObjectMapper().convertValue(
							a, Map.class));
				} else {
					assets.add(a);
					for (int j = pos; j < assets.size() - 1; j++) {
						swapAssets(assets, j, assets.size() -1 );
					}
					for (Asset as:assets) {
						assetRepository.update(as.getAssetId(), new ObjectMapper().convertValue(
								as, Map.class));
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				return new StandardResponse(false, e.toString());
			}
		}
		return new StandardResponse(true, "updated Successfully");
	}
	
	private void swapAssets(List<Asset> assets, int pos1, int pos2) {
		Asset one = assets.get(pos1);
		Asset two = assets.get(pos2);
		long tmpWeight = one.getWeight();
		one.setWeight(two.getWeight());
		two.setWeight(tmpWeight);
		assets.set(pos1, two);
		assets.set(pos2, one);
		
	}
	
	

}
