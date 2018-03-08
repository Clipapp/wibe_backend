package com.wibe.backend.threads;

import java.util.List;

import com.wibe.backend.dto.TagInfoDTO;
import com.wibe.backend.entities.models.Tag;
import com.wibe.backend.library.TagLib;
import com.wibe.backend.repositories.TagRepository;

public class TagDTOUpdateThread implements Runnable{
	
	private TagLib taglib = TagLib.getInstance(); 
	
	private Tag tag;
	private TagRepository tagRepository;
	private List<TagInfoDTO> tagdto;
	private int pos;
	private String lan;
	
	public TagDTOUpdateThread(Tag tag, TagRepository tagRepository,
			List<TagInfoDTO> tagdto, int pos, String lan) {
		// TODO Auto-generated constructor stub
		this.tag = tag;
		this.tagRepository =tagRepository;
		this.tagdto = tagdto;
		this.pos = pos;
		this.lan = lan;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		TagInfoDTO t = new TagInfoDTO(tag, lan); 
		taglib.addTagCounts(t, tagRepository, tag.getName());
		tagdto.set(pos, t);
		
	}

}
