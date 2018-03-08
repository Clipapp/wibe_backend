package com.wibe.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wibe.backend.entities.models.Asset;
import com.wibe.backend.entities.models.Banner;
import com.wibe.backend.responses.StandardResponse;
import com.wibe.backend.services.CreationServiceImpl;

@Controller
public class CreationController {
	
	private final CreationServiceImpl creationService;
	
	@Autowired
	public CreationController(CreationServiceImpl creationService) {
		// TODO Auto-generated constructor stub
		this.creationService = creationService;
	}
	
	@RequestMapping(value="/assets", method = RequestMethod.POST)
	public @ResponseBody StandardResponse uploadAsset(
			@RequestParam(value = "file") MultipartFile file,
			@RequestParam(value = "thumbnail") MultipartFile thumbnail,
			@RequestParam(value = "type") String type,
			@RequestParam(value = "name") String name,
			@RequestParam(value = "position", required = false, defaultValue = "0")int pos,
			@RequestParam(value = "live", required = false, defaultValue = "true")boolean live){
		return creationService.uploadAsset(file, thumbnail, type, name, live, pos);		
	}
	
	@RequestMapping(value="/assets/{type}", method = RequestMethod.GET)
	public @ResponseBody List<Asset> getAssets(@PathVariable("type") String type){
		return creationService.getAssetsBytType(type);
	}
	
	@RequestMapping(value="/assets/{assetId}", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updateAssets(@PathVariable("assetId") long assetId,
			@RequestParam(value = "pos") int pos,
			@RequestParam(value = "active") boolean active){
		return creationService.updateAsset(assetId, pos, active);
	}
	
	@RequestMapping(value="/asset/banner", method = RequestMethod.GET)
	public @ResponseBody Banner getBanner(
			@RequestParam(value = "appLan",required=false, defaultValue="hindi")String lan){
		return creationService.getBanner(lan);
	}
	
	@RequestMapping(value="/tags", method = RequestMethod.GET)
	 public @ResponseBody List<String> getTags(){
	    return creationService.getUpTags();
	 }
	
	@RequestMapping(value="/banner", method = RequestMethod.POST)
	public @ResponseBody StandardResponse uploadBanner(
			@RequestParam(value = "file") MultipartFile file,
			@RequestParam(value = "lan") String lan,
			@RequestParam(value = "tag") String tag,
			@RequestParam(value = "duration") int duration,
			@RequestParam(value = "height", required = false, defaultValue="200") int height,
			@RequestParam(value = "width", required = false, defaultValue="84") int width,
			@RequestParam(value = "live", required = false, defaultValue = "true")boolean live){
		return creationService.uploadBanner(file, lan, tag, duration, height, width, live);	
	}
	
	@RequestMapping(value="/banner", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updateBanner(
			@RequestParam ("lan") String lan,
			@RequestParam("live") boolean status){
		
		return creationService.updateBanner(lan, status);
	}
	

}
