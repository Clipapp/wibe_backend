package com.wibe.backend.repositories;

import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wibe.backend.entities.models.Counter;

@RepositoryRestResource(exported=false)
public interface CounterRepository extends GraphRepository<Counter> {

	@Query("MATCH (counter:Counter) WHERE counter.counter = {counter} RETURN counter")
	public Counter findByCounterId(@Param("counter")String counter);
	
	@Query("MATCH (counter:Counter) WHERE counter.counter = {counter} SET counter.seq = counter.seq + 1 "
			+ "RETURN counter")
	public Counter findAndIncreaseByCounterId(@Param("counter")String counter);
	
	
	@Query("MERGE (w:Counter{counter:{counter}}) "
			+ "ON CREATE set w = {counter}"
			+ "ON MATCH  set w = {counter} RETURN w")
	Counter update(@Param("counter") String name, @Param("counter") Map<String, Object> counter);
}
