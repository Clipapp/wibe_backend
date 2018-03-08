package com.wibe.backend.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wibe.backend.entities.models.Word;

import java.util.List;

@RepositoryRestResource(exported=false)
public interface WordRepository extends GraphRepository<Word>{
	
	@Query("Match (n:Word) return n")
	List<Word> getWords();
	
	@Query("MATCH (n:Word{name:{key}}) return n")
	Word getWordByKey(@Param("key") String key);
}
