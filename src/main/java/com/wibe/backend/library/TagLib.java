package com.wibe.backend.library;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.wibe.backend.dto.TagInfoDTO;
import com.wibe.backend.entities.models.Tag;
import com.wibe.backend.repositories.TagRepository;
import com.wibe.backend.threads.TagDTOUpdateThread;

public class TagLib {
	
	private static TagLib taglib;
	
	private TagLib(){
		
	}
	
	public static TagLib getInstance(){
		if (taglib == null){
			taglib = new TagLib();
		}
		return taglib;
	}

	public void createTagDTO(List<Tag> tags, List<TagInfoDTO> tagdto,
			TagRepository tagRepository, String lan){
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		for (int i =0; i< tags.size(); i++){
			Runnable worker = new TagDTOUpdateThread(tags.get(i), tagRepository, tagdto,
					i, lan);
			executor.execute(worker);
		}
		executor.shutdown();
		while (!executor.isTerminated()){
			
		}
	}
	
	public void addTagCounts(TagInfoDTO tagdto, TagRepository tagRepo, String name){
		long count = tagRepo.getNumWibesTag(name);
		if (count < 400){
			tagdto.setNumWibes(tagRepo.getNumWibesRel(name));
		} else {
			tagdto.setNumWibes(count);
		}
	}
}
