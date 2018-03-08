package com.wibe.backend.repositories;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wibe.backend.entities.models.Category;

@RepositoryRestResource(exported=false)
public interface CategoryRepository extends GraphRepository<Category>{

	@Query("MATCH (c:Category{name:{cat}}), (w:Wibe{wibeId:{wid}}) "
			+ "MERGE (w)-[cat:CATEGORY]->(c) ON CREATE SET cat.time = timestamp() ")
	void catVideo(@Param("wid") long wid, @Param("cat") String cat);
	
	@Query("MATCH (:Category{name:{name}})<-[:CATEGORY]-(w:Wibe) RETURN count(*)")
	long getPostCount(@Param("name") String name);
	
	@Query("MATCH (c:Category{name:{name}}) RETURN c")
	Category getByCatName(@Param("name") String name);
	
	@Query("MATCH (u:User{userId:{uid}}), (c:Category{name:{name}}) "
			+ "MERGE (u)-[f:FOLLOW]->(c) ON CREATE SET f.time = timestamp()")
	void followCat(@Param("uid")long uid, @Param("name") String name);
	
	@Query("MATCH (u:User{userId:{uid}})-[f:FOLLOW]->(c:Category{name:{name}})"
			+ " DELETE f")
	void unfollowCat(@Param("uid")long uid, @Param("name") String name);
	
	@Query("MATCH (u:User)-[:FOLLOW]->(:Category{name:{name}}) RETURN count(*)")
	long getFollowerCount(@Param("name") String name);
	
	@Query("MATCH (c:Category) WHERE c.live=true RETURN c")
	List<Category> getCategories();
	
	@Query("MATCH (u:User{userId:{uid}})-[f:FOLLOW]->(c:Category{name:{name}}) RETURN count(*)")
	int verifyFollower(@Param("uid") long uid, @Param("name") String name);	
	
}
