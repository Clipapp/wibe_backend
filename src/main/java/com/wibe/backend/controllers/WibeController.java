package com.wibe.backend.controllers;


import java.util.List;

import javax.servlet.http.HttpServletResponse;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wibe.backend.dto.CategoryInfoDTO;
import com.wibe.backend.dto.TagInfoDTO;
import com.wibe.backend.dto.WibeDTO;
import com.wibe.backend.dto.WibeInfoDTO;
import com.wibe.backend.entities.models.WibeList;
import com.wibe.backend.library.MajorCities;
import com.wibe.backend.library.MajorCities.Location;
import com.wibe.backend.responses.StandardResponse;
import com.wibe.backend.services.WibeServiceImpl;

@Controller
public final class WibeController {

	private final WibeServiceImpl wibeService;
	
	@Autowired
	public WibeController(WibeServiceImpl wibeService){
		this.wibeService = wibeService;
	}
	
	@RequestMapping(value = "wibes/{id}/preview", method = RequestMethod.GET)
	public @ResponseBody void getWibePreview(@PathVariable("id") long id,
			HttpServletResponse response){
		wibeService.previewWibe(response, id);
	}
	
	@RequestMapping (value = "wibes", method = RequestMethod.POST)
	public @ResponseBody StandardResponse uploadWibe(@RequestParam(value= "file", required= true) MultipartFile file,
			@RequestParam(value = "uploaderId", required = true)long uid,
			@RequestParam(value = "description", required = false) String desc,
			@RequestParam(value = "track", required = false) String music,
			@RequestParam(value = "artist", required = false) String artist,
			@RequestParam(value = "categories", required = true) String cat,
			@RequestParam(value = "tags", required = false, defaultValue = "") String tags,
			@RequestParam(value = "ext", required = true) String ext,
			@RequestParam(value = "lat", required = false) String latitude,
			@RequestParam(value = "lon", required = false) String longitude,
			@RequestParam(value = "height") int height,
			@RequestParam(value = "width") int width,
			@RequestParam(value = "postToFollowers", required = false, defaultValue = "true") boolean isPrivate){
		double lat, lon;
		try {
			lat = Double.parseDouble(latitude);
			lon = Double.parseDouble(longitude);
		} catch (Exception e) {
			// TODO: handle exception
			lat = 0.0;
			lon = 0.0;
		}
		if (lat == 0.0 && lon == 0.0){
			Location loc = MajorCities.randomCity();
			lat = loc.getLat();
			lon = loc.getLon();
		}
		WibeInfoDTO wibe = wibeService.uploadWibe(file, uid, desc,
				music, artist, cat, tags, ext, lat, lon, height, width, !isPrivate);
		if (wibe == null){
			return new StandardResponse(false, "Failed to upload file");
		}
		return new StandardResponse(true, wibe);
	}
	
	@RequestMapping (value = "wibers/{id}/wibes", method = RequestMethod.GET)
	public @ResponseBody List<WibeDTO> getUserWibes(
			@RequestParam(value = "type", required = false, defaultValue = "general") String type,
			@RequestParam(value = "page", required = false, defaultValue = "0") long page,
			@RequestParam(value = "limit", required = false, defaultValue = "12") long limit,
			@RequestParam(value = "lat", required = false, defaultValue = "0.0") double lat,
			@RequestParam(value = "lon", required = false, defaultValue = "0.0")double lon,
			@RequestParam(value = "radius", required = false, defaultValue = "250.0") double radius,
			@RequestParam(value = "lan", required = false, defaultValue = "hindi") String lan,
			@RequestParam(value = "zeerotime", required = false, defaultValue = "0") long zeroTime,
			@RequestParam(value = "seed", required = false, defaultValue = "0") long seed,
			@RequestHeader(value = "token", required = false) String token,
			@PathVariable("id") Long id){
		if(limit >25){
			limit = 25;
		}
		if (radius > 250){
			radius = (float) 250.0;
		}
		return wibeService.getWibes(type, id, page, limit, lat, lon, radius, token, lan, zeroTime, seed);
	}
	
	@RequestMapping( value ="tags/{name}", method = RequestMethod.GET)
	public @ResponseBody TagInfoDTO getTaginfo(
			@PathVariable("name") String name,
			@RequestParam(value = "lan", required = false, defaultValue="")String lan){
		return wibeService.getTagInfo(name.toLowerCase());
		
	}
	
	@RequestMapping( value ="category/{name}", method = RequestMethod.GET)
	public @ResponseBody CategoryInfoDTO getCategoryinfo(
			@PathVariable("name") String name,
			@RequestParam(value = "uid" )long uid,
			@RequestParam(value="lan", required = false, defaultValue = "english") String lan){
		return wibeService.getCategoryInfo(name.toLowerCase(), uid, lan.toLowerCase());
		
	}
	
	@RequestMapping (value = "wibes", method = RequestMethod.GET)
	public @ResponseBody WibeInfoDTO getWibe(
			@RequestParam("wid") long wid,@RequestParam("uid") long uid){
		return wibeService.getWibe(wid, uid);
	}
	
	@RequestMapping (value = "wibes", method = RequestMethod.DELETE)
	public @ResponseBody StandardResponse deleteWibe(
			@RequestParam("wid") long wid,
			@RequestParam(value = "status", required = false, defaultValue = "false") boolean status,
			@RequestParam(value = "tags", required = false, defaultValue = "") String tags,
			@RequestParam(value ="censor", required = false, defaultValue="false")boolean cen){
		return wibeService.deleteWibe(wid, status, cen, tags);
	}
	
	@RequestMapping (value = "wibes/explore", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updateWibeList(
			@RequestBody WibeList list){
		return wibeService.updateList(list);
	}
	
	@RequestMapping (value = "wibes/{wid}/restrict", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse restricComment(
			@PathVariable("wid") long wid, @RequestHeader("token") String token){
		return wibeService.restrictComments(wid, true, token);
	}
	
	@RequestMapping (value = "wibes/{wid}/restrict", method = RequestMethod.DELETE)
	public @ResponseBody StandardResponse unrestricComment(
			@PathVariable("wid") long wid, @RequestHeader("token") String token){
		return wibeService.restrictComments(wid, false, token);
	}
	
	@RequestMapping ( value = "wibes/{wid}/delete", method = RequestMethod.DELETE)
	public @ResponseBody StandardResponse deleteVideo(
			@PathVariable("wid") long wid,
			@RequestHeader("token") String token){
		return wibeService.deleteVideo(wid, token);
	}
	
	@RequestMapping (value = "wibes/{wid}/share/whatsapp", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updateWAShareCount(
			@PathVariable("wid") long wid, 
			@RequestHeader("token") String token){
		return wibeService.increaseWhatsAppShare(token, wid);
	}
	
	@RequestMapping (value = "wibes/{wid}/share", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updateShareCount(
			@PathVariable("wid") long wid,
			@RequestHeader("token") String token){
		return wibeService.increaseShareCount(token, wid);
	}
	
	@RequestMapping (value = "wibes/{wid}/download", method = RequestMethod.PUT)
	public @ResponseBody StandardResponse updateDownloadCount(
			@PathVariable("wid") long wid,
			@RequestHeader("token") String token){
		return wibeService.increaseDownloadCount(token, wid);
	}
	
}