package com.wibe.backend.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wibe.backend.entities.QueryResults.WibeObj;
import com.wibe.backend.entities.QueryResults.WibeObjIds;
import com.wibe.backend.entities.models.Slot;
import com.wibe.backend.entities.models.User;
import com.wibe.backend.entities.models.Wibe;

@RepositoryRestResource(exported=false)
public interface WibeRepository extends GraphRepository<Wibe>{
	
	@Query("MATCH (w:Wibe{wibeId:{wid}}) WHERE (w.deleted = false AND w.approved = true)"
			+ " OR (w.deleted = true AND w.approved = false)  RETURN w")
	Wibe findByWibeId(@Param("wid") Long wid);
	
	@Query("MATCH (w:Wibe{wibeId:{wid}}) RETURN w")
	Wibe getByWibeId(@Param("wid") Long wid);
	
	@Query("MERGE (w:Wibe{wibeId:{wibeId}}) "
			+ "ON CREATE set w = {wibe}, w.uploadedAt = timestamp() "
			+ "ON MATCH  set w = {wibe} RETURN w")
	Wibe update(@Param("wibeId") long id, @Param("wibe") Map<String, Object> wibe);
	
	@Query("MATCH (w:Wibe) WHERE w.language in {lans} "
			+ "WITH w ORDER BY w.uploadedAt DESC SKIP 0 LIMIT 2000 "
			+ "MATCH (w)<-[:UPLOAD]-(u:User) WHERE NOT (u)-[:BLOCKED]-(:User{token:{token}}) "
			+ "AND w.deleted = false AND w.isPrivate = false AND w.approvedAt < {time} "
			+ "RETURN w AS wibe, u AS user ORDER BY w.uploadedAt DESC SKIP {offset} LIMIT {limit}")
	List<WibeObj> getListWibes(@Param("offset") Long offset, @Param("limit") Long limit,
			@Param("token") String token, @Param("time") long time, 
			@Param("lans") List<String> lans);
	
	@Query("MATCH (w:Wibe)<-[:UPLOAD]-(u:User) "
			+ "WHERE w.wibeId > {wibeCount} - 10000 AND w.language in {lans} "
			+ "AND w.deleted = false AND w.isPrivate = false AND w.approvedAt < {time} "
			+ "RETURN w AS wibe, u AS user ORDER BY w.wibeId DESC SKIP {offset} LIMIT {limit}")
	List<WibeObj> getListWibes2(@Param("offset") Long offset, @Param("limit") Long limit,
			@Param("wibeCount") long wibeCount, @Param("time") long time, 
			@Param("lans") List<String> lans);
	
	@Query("MATCH (u:User{userId:{uid}})-[:SUBSCRIBED]->(b:User)-[:UPLOAD]->(w:Wibe) "
			+ "WHERE NOT (b)-[:BLOCKED]-(u) AND w.deleted = false "
			+ "AND w.isPrivate = false AND w.approvedAt < {time} "
			+ "RETURN w AS wibe, b AS user ORDER BY w.uploadedAt DESC SKIP {offset} LIMIT {limit}")
	List<WibeObj> getPersonalWibes(@Param("offset") Long offset, @Param("limit") Long limit,
			@Param("uid") Long uid, @Param("time") long time);
	
	@Query("MATCH (u:User{userId:{id}})-[:UPLOAD]->(w:Wibe) "
			+ "WHERE NOT (u)-[:BLOCKED]-(:User{token:{rtoken}}) AND w.deleted = false "
			+ "AND w.isPrivate = false AND w.uploadedAt < timestamp() "
			+ "RETURN w AS wibe, u AS user ORDER BY w.uploadedAt DESC SKIP {offset} LIMIT {limit}")
	List<WibeObj> getUploads(@Param("id") Long uid, @Param("offset") Long offset,
			@Param("limit") Long limit, @Param("rtoken") String token);
	
	@Query("MATCH (u:User{userId:{id}})-[:UPLOAD]->(w:Wibe) WHERE "
			+ "(w.deleted = true AND w.approved = false) OR "
			+ "(w.deleted = false AND w.approved = true) "
			+ "RETURN w AS wibe, u AS user ORDER BY w.uploadedAt DESC SKIP {offset} LIMIT {limit}")
	List<WibeObj> getMyUploads(@Param("id") Long uid, @Param("offset") long offset,
			@Param("limit") long limit);
	
	@Query("MATCH (:User{userId:{id}})-[:FAVOURITE]->(w:Wibe)<-[:UPLOAD]-(u:User) "
			+ "WHERE w.deleted = false "
			+ "RETURN w AS wibe, u AS user SKIP {offset} LIMIT {limit}")
	List<WibeObj> getFavourites(@Param("id") Long uid, @Param("offset") Long offset,
			@Param("limit") Long limit);
	
	
	//List<Wibe> getNearbyWibes(@Param("offset") long offset, @Param("limit") long limit,
	//@Param("lat") double lat, @Param("lon") double lon);
	
	@Query("MATCH(:User)-[:FAVOURITE]->(:Wibe{wibeId:{wid}}) RETURN count(*)")
	int getFavouriteCount(@Param("wid") Long wid);
	
	@Query("MATCH(:User)-[:LIKED]->(:Wibe{wibeId:{wid}}) RETURN count(*)")
	int getLikeCount(@Param("wid") Long wid);
	
	@Query("MATCH(:User)-[:COMMENT]->(:Wibe{wibeId:{wid}}) RETURN count(*)")
	int getCommentCount(@Param("wid") Long wid);
	
	@Query("MATCH(:User)-[:SHARED]->(:Wibe{wibeId:{wid}}) RETURN count(*)")
	int getShareCount(@Param("wid") Long wid);
	
	@Query("MATCH(:User)-[v:VIEWED]->(:Wibe{wibeId:{wid}}) RETURN sum(v.count)")
	int getViewCount(@Param("wid") Long wid);
	
	@Query("MATCH(:User{userId:{uid}})-[:LIKED]->(:Wibe{wibeId:{wid}}) RETURN count(*)")
	int verifyLiked(@Param("wid") long wid, @Param("uid") long uid);
	
	@Query("MATCH(:User{userId:{uid}})-[:FAVOURITE]->(:Wibe{wibeId:{wid}}) RETURN count(*)")
	int verifyFavourite(@Param("wid") long wid, @Param("uid") long uid);
	
	@Query("MATCH (n:User{userId:{uid}})-[:SUBSCRIBED]->(:User)-[:UPLOAD]->"
			+ "(w:Wibe{wibeId:{wid}}) RETURN count(*)")
	int verifyFollowing(@Param("wid") long wid, @Param("uid") long uid);
	
	@Query("MATCH (u:User{userId:{uid}}), (w:Wibe{wibeId:{wid}}) "
			+ "MERGE (u)-[up:UPLOAD]->(w) ON CREATE SET up.time = timestamp()")
	void updateUploader(@Param("wid") long wid, @Param("uid") long uid);
	
	@Query("MATCH (w:Wibe)<-[:UPLOAD]-(u:User) "
			+ "WHERE NOT (u)-[:BLOCKED]-(:User{userId:{uid}}) AND w.deleted = false "
			+ "AND w.isPrivate = false AND w.uploadedAt < timestamp() "
			+ "RETURN w AS wibe, u AS user ORDER BY w.uploadedAt DESC SKIP {offset} LIMIT {limit}")
	List<WibeObj> getRecentPosts(@Param("uid") long uid, 
			@Param("offset") long offset, @Param("limit") int limit);
	
	@Query("MATCH (w:Wibe)-[:HASHTAG]->(t:Tag{name:{tag}}) "
			+ "WHERE w.deleted = false AND w.isPrivate = false AND "
			+ "w.approvedAt < timestamp() "
			+ "WITH w SKIP {offset} LIMIT {limit} "
			+ "MATCH (u:User)-[:UPLOAD]->(w)"
			+ "RETURN w AS wibe, u AS user")
	List<WibeObj> getRecentPostsByTag(@Param("offset") long offset, @Param("limit") int limit,
			@Param("tag") String tag, @Param("uid") long uid);
	
	@Query("MATCH (u:User)-[:UPLOAD]->(w:Wibe)-[:CATEGORY]->(c:Category{name:{cat}}) "
			+ "WHERE w.deleted = false "
			+ "AND w.isPrivate = false AND w.uploadedAt < timestamp() "
			+ "RETURN w AS wibe, u AS user ORDER BY w.uploadedAt DESC SKIP {offset} LIMIT {limit}")
	List<WibeObj> getRecentPostsByCat(@Param("offset") long offset, @Param("limit") int limit,
			@Param("cat") String category);
	
	@Query("MATCH (:User)-[l:LIKED]->(w:Wibe)<-[:UPLOAD]-(u:User)"
			+ " WHERE l.time > {time} AND w.deleted = false "
			+ "AND w.isPrivate = false AND w.uploadedAt < timestamp() "
			+ "WITH w,u count(l)  as count RETURN w AS wibe, u AS user ORDER BY count DESC SKIP {offset} LIMIT {limit}")
	List<WibeObj> getPopularPosts(@Param("offset") long offset, @Param("limit") int limit,
			@Param("time") long time);
	
	@Query("MATCH (:User)-[l:LIKED]->(w:Wibe)-[:HASHTAG]->(t:Tag{name:{tag}}) "
			+ "where l.time > {time} AND "
			+ "NOT (w)<-[:UPLOAD]-(:User)-[:BLOCKED]-(:User{userId:{uid}}) "
			+ "AND w.deleted = false AND w.isPrivate = false AND w.uploadedAt < timestamp()"
			+ " WITH w, count(l)  as count MATCH (w)<-[UPLOAD]-(up:User) "
			+ "RETURN w AS wibe, up AS user ORDER BY count DESC SKIP {offset} LIMIT {limit}")
	List<WibeObj> getPopularPostsByTag(@Param("offset") long offset, @Param("limit") int limit,
			@Param("time") long time, @Param("tag") String tag, @Param("uid") long uid);
	
	@Query("MATCH (:User)-[l:LIKED]->(w:Wibe)-[:CATEGORY]->(c:Category{name:{cat}})"
			+ " where l.time > {time} AND w.deleted = false "
			+ "AND w.isPrivate = false AND w.uploadedAt < timestamp() "
			+ "WITH w, count(l)  as count MATCH (w)<-[UPLOAD]-(up:User) "
			+ "RETURN w AS wibe, up AS user ORDER BY count DESC SKIP {offset} LIMIT {limit}")
	List<WibeObj> getPopularPostsByCat(@Param("offset") long offset, @Param("limit") int limit,
			@Param("time") long time, @Param("cat") String category);
	
	@Query("MATCH (n:Wibe{wibeId:{wid}}) "
			+ "CALL spatial.addNode('geom',n) YIELD node RETURN node")
	Wibe addWibeToLayer(@Param("wid")long wid);
	
//	@Query("Match (n:Wibe) WHERE NOT EXISTS(n.bbox) CALL spatial.addNode('geom',n) "
//			+ "YIELD node RETURN node.wibeId")
//	void addWibesToLayer();
	
	@Query("CALL spatial.withinDistance('geom',{lat:{lat},lon:{lon}},{radius}) "
			+ "YIELD node, distance WHERE NOT (node)<-[:UPLOAD]-(:User)-[:BLOCKED]-(:User{token:{token}}) "
			+ "AND node.deleted = false AND node.isPrivate = false "
			+ "AND node.uploadedAt < {time} WITH node, distance MATCH (node)<-[:UPLOAD]-(up:User) "
			+ "RETURN node as wibe, up as user ORDER BY node.uploadedAt DESC, distance SKIP {offset} LIMIT {limit}")
	List<WibeObj> getNearbyWibes(@Param("offset")long offset,@Param("limit") long limit, 
			@Param("lat")double lat,@Param("lon") double lon,
			@Param("radius") double radius, @Param("token") String token, 
			@Param("time") long time);
	
	@Query("MATCH (n:Wibe)<-[:UPLOAD]-(u:User) "
			+ "WHERE n.approved = false AND n.lockedTill < timestamp() "
			+ "with n as wibe, u as user ORDER BY n.uploadedAt LIMIT 1 "
			+ "Match (wibe) set wibe.lockedTill = timestamp() + 300000 Return wibe, user")
	List<WibeObj> getPendingWibes();
	
	@Query("MATCH (w:Wibe) WHERE w.language in {lans} AND w.uploadedAt < {time} "
			+ "WITH w ORDER BY w.uploadedAt DESC SKIP 0 LIMIT 3000 "
			+ "MATCH (w)<-[:UPLOAD]-(u:User) WHERE NOT (u)-[:BLOCKED]-(:User{token:{token}}) "
			+ "AND w.deleted = false AND w.isPrivate = false RETURN w.wibeId")
	List<Long> getRecentWibes(@Param("token") String token, @Param("time") long time, 
			@Param("lans") List<String> lans);
	
	@Query("MATCH (w:Wibe)<-[:UPLOAD]-(u:User) WHERE w.wibeId > {wibeCount} - {num} "
			+ "AND w.language in {lans} "
			+ "AND w.deleted = false AND w.isPrivate = false AND w.approvedAt < {time} "
			+ "RETURN w.wibeId as wibeId, u.userId as uploaderId limit 3000")
	List<WibeObjIds> getRecentWibes2(@Param("wibeCount") long wibeCount, @Param("time") long time, 
			@Param("lans") List<String> lans, @Param("num") long scope);
	
	@Query("MATCH (:Tag{name:'created'})<-[:HASHTAG]-(w:Wibe)<-[:UPLOAD]-(u:User) "
			+ "WHERE w.deleted = false AND w.isPrivate = false AND w.approvedAt < {time} "
			+ "RETURN w.wibeId as wibeId, u.userId as uploaderId limit 3000")
	List<WibeObjIds> getCreatedWibes(@Param("time") long time);
	
	@Query("Match (w:Wibe)<-[:UPLOAD]-(u:User) where w.wibeId in {list} "
			+ "return w as wibe, u as user")
	List<WibeObj> getWibesList(@Param("list")List<Long> list);
	
	@Query("MATCH (up:User)-[:UPLOAD]->(w:Wibe)<-[f:FAVOURITE]-(u:User) "
            + "WHERE w.wibeId > {wibeCount} -15000 AND w.deleted = false "
            + "AND w.isPrivate = false "
            + "WITH w, up, count (f) as total_likes, "
            + "sum (CASE WHEN f.time > (timestamp() - 86400000)THEN 1 ELSE 0 END) as recent_likes "
            + "ORDER BY (recent_likes + recent_likes/total_likes * (w.whatsAppShare + "
            + "w.shareCount + w.downloadCount + w.numViews/8)) DESC SKIP {offset} LIMIT {limit} "
            + "RETURN w AS wibe,up AS user ")
	List<WibeObj> getTrendingWibes(@Param("offset") Long offset, @Param("limit") Long limit,
            @Param("token") String token, @Param("time") long time, @Param("wibeCount") long wibeCount);
	
	
	@Query("MATCH (w:Wibe{wibeId:{wid}})<-[:UPLOAD]-(:User{token:{token}}) RETURN w")
	Wibe getWibeByIdAndToken(@Param("wid") long wid, @Param("token") String token);
	
	@Query("MATCH (w:Wibe) RETURN count(*)")
	long getNumWibes();
	
	@Query("MATCH (:Wibe{wibeId:{wid}})<-[:COMMENT]-(u:User) Return u")
	List<User> findCommentators(@Param("wid") long wid);
	
	@Query("Match (:User{userId:{uid}})-[:VIEWED]->(:Wibe{wibeId:{wid}}) Return count(*)")
	int verifyView(@Param("wid") long wid, @Param("uid") long uid);
	
	@Query("Match (w:Wibe{wibeId:{wid}})<-[:UPLOAD]-(u:User) Return w as wibe, u as user")
	WibeObj getOne(@Param("wid") long wid);
	
	@Query("Match (s:Slot) where s.active = true return s order by s.slot")
	List<Slot> getSlots();
	
	@Query("MATCH (n:User{token:{token}})-[:SUBSCRIBED]->(u:User)-[:FAVOURITE]->(:Wibe{wibeId:{wid}})"
			+ " WHERE NOT (u)-[:BLOCKED]-(n) RETURN u SKIP {skip} LIMIT {limit}")
	List<User> getLikedByFreinds(@Param("token") String token, @Param("wid") long wid, 
			@Param("limit") int limit ,@Param("skip") int skip);
	
	@Query("MATCH (n:User{token:{token}})-[:SUBSCRIBED]->(u:User)-[:FAVOURITE]->(:Wibe{wibeId:{wid}})"
			+ " WHERE NOT (u)-[:BLOCKED]-(n) RETURN u.userId")
	List<Long> getLikedByFreindsId(@Param("token") String token, @Param("wid") long wid);
	
	@Query("MATCH(u:User)-[:FAVOURITE]->(:Wibe{wibeId:{wid}}) "
			+ "WHERE not (:User{token:{token}})-[:BLOCKED]-(u) AND NOT u.userId IN {friends}"
			+ "RETURN u SKIP {skip} LIMIT {limit} ")
	List<User> getLikedBy(@Param("token") String token, @Param("wid") long wid, 
			@Param("limit") int limit ,@Param("skip") int skip, @Param("friends") List<Long> friends);
}
