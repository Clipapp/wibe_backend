package com.wibe.backend.services;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.wibe.backend.dto.CategoryInfoDTO;
import com.wibe.backend.dto.TagInfoDTO;
import com.wibe.backend.dto.WibeDTO;
import com.wibe.backend.dto.WibeInfoDTO;
import com.wibe.backend.entities.models.WibeList;
import com.wibe.backend.responses.StandardResponse;

public interface WibeService {
	
	WibeInfoDTO uploadWibe(MultipartFile file, Long uid, String desc, String music, 
			String artist, String cat, String tags, String ext, double lat, double lon,
			int height, int width, boolean isPrivate);
	
	void previewWibe(HttpServletResponse response, Long wid);
	
	List<WibeDTO> getWibes(String type, long id, long page, long limit, double lat,
			double lon, double radius, String token, String lan, long zeroTime, long seed);
	
	TagInfoDTO getTagInfo(String name);
	
	CategoryInfoDTO getCategoryInfo(String name, long uid, String lan);
	
	WibeInfoDTO getWibe(long wid, long uid);
	
	StandardResponse deleteWibe(long wid, boolean status, boolean censor, String tags);
	
	StandardResponse restrictComments(long wid, boolean val, String token);
	
	StandardResponse deleteVideo(long wid, String token);
	
	StandardResponse increaseWhatsAppShare(String token, long wid);
	
	StandardResponse increaseShareCount(String token, long wid);
	
	StandardResponse increaseDownloadCount(String token , long wid);
	
	StandardResponse updateList(WibeList list);

}
