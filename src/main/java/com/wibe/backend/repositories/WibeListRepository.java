package com.wibe.backend.repositories;

import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wibe.backend.entities.models.WibeList;

@RepositoryRestResource(exported=false)
public interface WibeListRepository extends GraphRepository<WibeList>{
	
	@Query("MERGE (w:WibeList{listId:{listId}}) "
			+ "ON CREATE set w = {wibeList}, w.createdAt = timestamp() "
			+ "ON MATCH  set w = {wibeList}, w.updatedAt = timestamp() RETURN w")
	WibeList update(@Param("listId") long id, @Param("wibeList") Map<String, Object> wibe);
	

	@Query("MATCH (w:WibeList{listId:{id}}) RETURN w")
	WibeList findByListId(@Param("id") long id);

}
