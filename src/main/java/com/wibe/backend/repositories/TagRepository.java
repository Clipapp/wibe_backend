package com.wibe.backend.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wibe.backend.entities.models.Banner;
import com.wibe.backend.entities.models.Tag;
import com.wibe.backend.entities.models.UpTags;

import java.util.List;

@RepositoryRestResource(exported=false)
public interface TagRepository extends GraphRepository<Tag> {
	
	@Query("MATCH (t:Tag{name:{name}}) RETURN t")
	Tag findTagByName(@Param("name") String name);
	
	@Query("CREATE (t:Tag{name:{name},tagId:{tid},createdAt:timestamp(), wibeCount:0}) "
			+ "RETURN t")
	Tag create(@Param("tid") long tid, @Param("name") String name);
	
	@Query("MATCH (t:Tag{name:{name}}), (w:Wibe{wibeId:{wid}}) WITH t,w "
			+ "SET t.wibeCount = t.wibeCount + 1 with t,w "
			+ "MERGE (w)-[h:HASHTAG]->(t) ON CREATE SET h.time = timestamp()")
	void tagVideo(@Param("wid") long wid,@Param("name") String tag );
	
	@Query("MATCH (t:Tag) where t.name STARTS WITH {q} AND NOT t.name in ['created', 'gallery'] "
			+ "RETURN t SKIP {offset} LIMIT {limit}")
	List<Tag> suggestTag(@Param("q") String text, @Param("limit") int limit,
			@Param("offset") int offset);
	
	@Query("MATCH (c:Challenge{isChallenge:true, active:true}) return c")
	List<Tag> getChallenges();
	
	@Query("MATCH (:Tag{name:{name}})<-[:HASHTAG]-(w:Wibe) "
			+ "WHERE w.isPrivate = false "
			+ "AND w.deleted = false RETURN count(*)")
	long getNumWibesRel(@Param("name") String name);
	
	@Query("Match (t:Tag{name:{name}}) Return t.wibeCount")
	long getNumWibesTag(@Param("name") String name);
	
	@Query("MATCH (w:Wibe)-[h:HASHTAG]->(t:Tag) where w.uploadedAt > {time}"
			+ " AND NOT t.name in ['created', 'gallery'] WITH count(h) as count, t "
			+ "RETURN t ORDER BY count DESC SKIP {offset} LIMIT {limit}")
	List<Tag> getPopularTags(@Param("time") long time, 
			@Param("offset") long offset, @Param("limit") long limit);
	
	@Query ("Match (n:Banner) Where n.lan = {lan} and n.active = true return n limit 1")
	Banner getBanner(@Param("lan") String lan);
	
	@Query("Match (n:UpTags) return n limit 1")
	UpTags getUpTag();
}
