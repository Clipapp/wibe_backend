package com.wibe.backend.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wibe.backend.entities.QueryResults.Leader;
import com.wibe.backend.entities.QueryResults.Notification;
import com.wibe.backend.entities.models.User;

@RepositoryRestResource(exported=false)
public interface UserRepository extends GraphRepository<User> {
	
	@Query("MATCH (user:User) WHERE user.userId = {id} RETURN user")
	User findByUserId(@Param("id")Long id);
	
	@Query("MATCH (u:User{userId:{uid}}) where NOT (u)-[:BLOCKED]-(:User{token:{token}})"
			+ "RETURN u")
	User getUserProfile(@Param("uid") long uid, @Param("token") String token);	
	
	@Query("MATCH (u:User{token:{token}}) RETURN u")
	User findbyToken(@Param("token") String token);
	
	User findByEmail(@Param("email") String email);
	
	@Query("Merge (n:User{userId:{userId}}) "
			+ "ON CREATE set n = {user}, n.createdAt = timestamp() "
			+ "ON MATCH  set n = {user} RETURN n")
	User update(@Param("userId") long id, @Param("user") Map<String, Object> user);
	
	@Query("MATCH (user:User) WHERE user.facebookId = {id} RETURN user")
	User findByFbId(@Param("id") String id);
	
	@Query("MATCH (user:User) WHERE user.number = {number} RETURN user")
	User findByNumber(@Param("number")String number);
	
	@Query("Create(user:User{user}) SET user.createdAt = timestamp() RETURN user")
	User create(@Param("user") User user);
	
	@Query("MATCH (user:User) WHERE user.userId = {id} RETURN user")
	User findidByUserId(@Param("id")Long id);
	
	@Query("MATCH (user:User{userId :{uid}}), (wibe:Wibe{wibeId : {wid}})"
			+ " MERGE (user)-[f:FAVOURITE]->(wibe) "
			+ "ON CREATE SET f.time = timestamp() RETURN user")
	User addFavourite(@Param("uid") Long uid, @Param("wid") Long wid);
	
	@Query("MATCH (user:User{userId:{uid}})-[f:FAVOURITE]->(wibe:Wibe{wibeId:{wid}})"
			+ "DELETE f RETURN user")
	User removeFavourite(@Param("uid") Long uid, @Param("wid") Long wid);
	
	@Query("MATCH (u:User{userId :{uid}}), (s:User{userId :{sid}})"
			+ " MERGE (u)-[sr:SUBSCRIBED]->(s) "
			+ "ON CREATE SET sr.time = timestamp() RETURN u")
	User subscribe(@Param("uid") Long uid, @Param("sid") Long sid);
	
	@Query("MATCH (u:User{userId:{uid}})-[sr:SUBSCRIBED]->(s:User{userId:{sid}})"
			+ "DELETE sr RETURN u")
	User unsubscribe(@Param("uid") Long uid, @Param("sid") Long sid);
	
	@Query("MATCH (user:User{userId :{uid}}), (wibe:Wibe{wibeId : {wid}})"
			+ " MERGE (user)-[l:LIKED]->(wibe) "
			+ "ON CREATE SET l.time = timestamp() RETURN user")
	User like(@Param("uid") Long uid, @Param("wid") Long wid);
	
	@Query("MATCH (user:User{userId:{uid}})-[l:LIKED]->(wibe:Wibe{wibeId:{wid}})"
			+ "DELETE l RETURN user")
	User unlike(@Param("uid") Long uid, @Param("wid") Long wid);
	
	@Query("MATCH (u:User{userId:{uid}}), (w:Wibe{wibeId:{wid}}) "
			+ "CREATE(u)-[c:COMMENT{commentId:{cid}}]->(w) "
			+ "SET c.date = timestamp() RETURN u")
	User comment(@Param("uid") Long uid,@Param("wid") Long wid, 
			@Param("cid") String cid);
	
	@Query("MATCH (u:User{userId:{uid}})-[c:COMMENT{commentId:{cid}}]->(w:Wibe{wibeId:{wid}})"
			+ " DELETE c RETURN u")
	User uncomment(@Param("uid") Long uid,@Param("wid") Long wid, @Param("cid") String cid);
	
	@Query("MATCH (:User{userId:{uid}})-[:FAVOURITE]->(w:Wibe)  "
			+ "WHERE w.deleted = false RETURN count(*)")
	int getFavouriteCount(@Param("uid") Long uid);
	
	@Query("MATCH (:User{userId:{uid}})-[:UPLOAD]->(w:Wibe) "
			+ "WHERE w.deleted = false AND w.isPrivate = false "
			+ "AND w.uploadedAt < timestamp() RETURN count(*)")
	int getUploadCount(@Param("uid") Long uid);
	
	@Query("MATCH (:User{userId:{uid}})-[:UPLOAD]->(w:Wibe) WHERE"
			+ "(w.deleted = true AND w.approved = false) OR "
			+ "(w.deleted = false AND w.approved = true) "
			+ "RETURN count(*)")
	int getMyUploadCount(@Param("uid") Long uid);
	
	@Query("MATCH (u:User{userId:{uid}})<-[:SUBSCRIBED]-(s:User) "
			+ "WHERE NOT (u)-[:BLOCKED]-(s) RETURN count(*)")
	int getFollowerCount(@Param("uid") Long uid);
	
	@Query("MATCH (u:User{userId:{uid}})-[:SUBSCRIBED]->(s:User) "
			+ "WHERE NOT (u)-[:BLOCKED]-(s) RETURN count(*)")
	int getFollowingCount(@Param("uid") Long uid);
	
	@Query("MATCH (w:Wibe)<-[:UPLOAD]-(:User{userId:{uid}})"
			+ " WHERE w.deleted = false RETURN sum(w.numViews)")
	int getViewCount(@Param("uid") Long uid);
	
	@Query("MATCH (:User{userId:{uid}})<-[:SUBSCRIBED]-(:User{userId:{sid}}) RETURN count(*)")
	int verifySubscription(@Param("uid") Long uid, @Param("sid") Long sid);
	
	@Query("MATCH (:User{userId:{uid}})<-[:SUBSCRIBED]-(:User{token:{token}}) RETURN count(*)")
	int verifySubscription(@Param("uid") Long uid, @Param("token") String token);
	
	@Query("MATCH (:User{userId:{uid}})-[:SUBSCRIBED]->(:User{number:{number}}) RETURN count(*)")
	int verifySubscriptionViaNumber(@Param("uid") Long uid, @Param("number") String number);
	
	@Query("MATCH (u:User) WHERE (u.username STARTS WITH {q} OR  "
			+ "u.nameLower STARTS WITH {q}) AND NOT (u)-[:BLOCKED]-(:User{userId:{uid}}) "
			+ "RETURN u SKIP {offset} LIMIT {limit}")
	List<User> suggestUser(@Param("q") String text, @Param("limit") int limit, 
			@Param("offset") int offset, @Param("uid") long uid);
	
	@Query("MATCH (u:User) WHERE u.username = {q} RETURN count(*)")
	int countUsernames(@Param("q") String text);
	
	@Query("MATCH (w:Wibe)<-[:UPLOAD]-(ul:User) "
			+ "where w.uploadedAt > timestamp() - 1*86400000 with ul, "
			+ "sum (w.numViews/8 + w.shareCount + w.downloadCount + w.whatsAppShare) as count "
			+ "RETURN count,ul.name as fullname,ul.username as username,ul.userId as userId, "
			+ "ul.imgUrl as imgUrl ORDER BY count DESC SKIP {offset} limit {limit}")
	List<Leader> getLeaderBoard(@Param("time") long time, @Param("offset") int offset,
			@Param("limit") int limit, @Param("token") String token);
	
	@Query("MATCH (u:User{userId:{uid}}), (w:Wibe{wibeId:{wid}}) "
			+ "MERGE (u)-[v:VIEWED]->(w) "
			+ "ON CREATE SET v.count = 1, v.lastViewed = timestamp(), w.numViews = w.numViews + 1 "
			+ "ON MATCH SET v.count = v.count + 1, v.lastViewed = timestamp(), w.numViews = w.numViews + 1")
	void updateViewCount(@Param("uid") long uid, @Param("wid") long wid);
	
	@Query("MATCH (u:User{token:{token}}), (w:Wibe{wibeId:{wid}}) "
			+ "MERGE (u)-[v:SHARED]->(w) "
			+ "ON CREATE SET v.wShare = 1, v.oShare=0, v.lastShared = timestamp() "
			+ "ON MATCH SET v.wShare = v.wShare + 1, v.lastShared = timestamp()")
	void updateWShareCount(@Param("token") String token, @Param("wid") long wid);
	
	@Query("MATCH (u:User{token:{token}}), (w:Wibe{wibeId:{wid}}) "
			+ "MERGE (u)-[v:SHARED]->(w) "
			+ "ON CREATE SET v.wShare = 0, v.oShare=1, v.lastShared = timestamp() "
			+ "ON MATCH SET v.oShare = v.oShare + 1, v.lastShared = timestamp()")
	void updateOShareCount(@Param("token") String token, @Param("wid") long wid);
	
	@Query("MATCH (u:User{token:{token}}), (w:Wibe{wibeId:{wid}}) "
			+ "MERGE (u)-[v:DOWNLOADED]->(w) "
			+ "ON CREATE SET v.count = 1, v.lastDownloaded = timestamp() "
			+ "ON MATCH SET v.count = v.count + 1, v.lastDownloaded = timestamp()")
	void updateDownloadCount(@Param("token") String token, @Param("wid") long wid);

	@Query("MATCH (f:User{number:{number}}), (u:User{userId:{uid}}) "
			+ "MERGE (u)-[s:SUBSCRIBED]->(f) "
			+ "ON CREATE set s.time = timestamp() RETURN f")
	User subscribeContact(@Param("number") String number,@Param("uid") long uid );
	
	
	@Query("MATCH (u:User)-[n:NOTIFY]->(up:User{userId:{uid}}) "
			+ "WHERE NOT (u)-[:BLOCKED]-(up) "
			+ "RETURN n.time as time, n.type as notifType, n.wid as wid, "
			+ "n.message as message, u.userId as uid, u.imgUrl as userThumbnail,"
			+ " n.url as wibeThumbnail SKIP {offset} LIMIT {limit}")
	List<Notification> getNotifications(@Param("uid") long uid, 
			@Param("offset") long offset, @Param("limit") long limit);
	
	@Query("MATCH (u:User{userId:{uid}}), (up:User{userId:{upid}})"
			+ "CREATE (u)-[n:NOTIFY{type:{type}, message:{message},"
			+ " wid:{wid}, time:timestamp(), uid:{uid}, url:{turl}}]->(up)")
	void createNotification(@Param("message") String message, @Param("type") String type,
			@Param("uid") long uid, @Param("upid") long upid, @Param("wid") long wid,
			@Param("turl") String wibeThumbnail);
	
	@Query("MATCH (u:User{userId:{uid}}), (up:User{userId:{upid}})"
			+ "MERGE (u)-[n:NOTIFY{type:{type}, message:{message},"
			+ " wid:{wid}, uid:{uid}}]->(up) SET n.time = timestamp(), n.url = {turl}")
	void updateNotification(@Param("message") String message, @Param("type") String type,
			@Param("uid") long uid, @Param("upid") long upid, @Param("wid") long wid,
			@Param("turl") String wibeThumbnail);
	
	@Query("MATCH (u:User{userId:{uid}})-[n:NOTIFY{type:{type}, message:{message},"
			+ " wid:{wid}, uid:{uid}}]->(up:User{userId:{upid}}) DELETE n")
	void deleteNotification(@Param("message") String message, @Param("type") String type,
			@Param("uid") long uid, @Param("upid") long upid, @Param("wid") long wid,
			@Param("turl") String wibeThumbnail);
	
	@Query("MATCH (u:User{userId:{uid}})<-[:SUBSCRIBED]-(n:User) "
			+ "WHERE NOT (u)-[:BLOCKED]-(n) "
			+ "RETURN n SKIP {offset} LIMIT {limit}")
	List<User> getFollowers(@Param("uid") long uid, @Param("offset") long offset,
			@Param("limit") long limit);
	
	@Query("MATCH (u:User{userId:{uid}})<-[:SUBSCRIBED]-(n:User) "
			+ "WHERE NOT (u)-[:BLOCKED]-(n) "
			+ "AND (toLower(n.name) starts with {q} OR toLower(n.username) starts with {q})"
			+ "RETURN n SKIP {offset} LIMIT {limit}")
	List<User> suggestFollowers(@Param("token") String token, @Param("uid") long uid,
			@Param("q") String q, @Param("offset") long offset, @Param("limit") long limit);
	
	@Query("MATCH (u:User{userId:{uid}})-[:SUBSCRIBED]->(n:User) "
			+ "WHERE NOT (u)-[:BLOCKED]-(n) "
			+ "RETURN n SKIP {offset} LIMIT {limit}")
	List<User> getFollowings(@Param("uid") long uid, @Param("offset") long offset,
			@Param("limit") long limit);
	
	@Query("MATCH (u:User{token:{token}})-[:SUBSCRIBED]->(n:User) "
			+ "WHERE NOT (u)-[:BLOCKED]-(n) AND "
			+ "(toLower(n.name) starts with {q} OR toLower(n.username) starts with {q}) "
			+ "RETURN n SKIP {offset} LIMIT {limit}")
	List<User> suggestFollowings(@Param("token") String token, @Param("q") String q,
			@Param("offset") long offset,@Param("limit") long limit);
	
	@Query("MATCH (u:User{googleId:{id}}) RETURN u")
	User findByGoogleId(@Param("id") String id);
	
	@Query("MATCH (u:User{userId :{uid}}), (s:User{userId :{bid}})"
			+ " MERGE (u)-[sr:BLOCKED]->(s) "
			+ "ON CREATE SET sr.time = timestamp() RETURN u")
	User blockUser(@Param("uid") long uid, @Param("bid") long bid);
	
	@Query("MATCH (u:User{userId:{uid}})-[sr:BLOCKED]->(s:User{userId:{bid}})"
			+ "DELETE sr RETURN u")
	User unblockUser(@Param("uid") long uid, @Param("bid") long bid);
	
	@Query("MATCH (:User{token:{token}})-[:BLOCKED]->(u:User) Return u")
	List<User> getBlockedUsers(@Param("token") String token);
	
	@Query("MATCH (:User{token:{token}})-[:BLOCKED]->(u:User) Return u.userId")
	List<Long> getBlockedUsersId(@Param("token") String token);
	
	@Query("MATCH (s:User{userId:{sid}}), (r:User{userId:{rid}}) "
			+ "MERGE (s)-[m:MESSAGED]-(r) "
			+ "ON CREATE SET m = {message} "
			+ "ON MATCH SET m = {message} RETURN m")
	Map<String, Object> message(@Param("sid") long sid, @Param("rid") long rid, 
			@Param("message") Map<String, Object> message);
	
	@Query("MATCH(u:User{token:{token}})-[m:MESSAGED]-(r:User) "
			+ "WHERE NOT (u)-[:BLOCKED]-(r) "
			+ "RETURN m.senId as senId, m.recId as recId, m.txtMessage as txtMessage, "
			+ "m.time as time, m.sharedProfileUserId as sharedProfileUserId, "
			+ "m.SharedProfileUserPicUrl as sharedProfileUserPicUrl, "
			+ "m.sharedProfileUsername as sharedProfileUsername,"
			+ "m.sharedProfileName as sharedProfileName, m.sharedWibeId as sharedWibeId, "
			+ "m.sharedWibeUploaderPicUrl as sharedWibeUploaderPicUrl, "
			+ "m.sharedWibeThumbnail as sharedWibeThumbnail, "
			+ "m.sharedWibeUploaderName as sharedWibeUploaderName, m.chatType as chatType,"
			+ "r.username as recUsername, r.imgUrl as recImgUrl "
			+ "ORDER BY m.time DESC SKIP {offset} LIMIT {limit}")
	List<Map<String, Object>> getRecentChats(@Param("token") String token, @Param("limit")
	int limit, @Param("offset") int offset);
	
	@Query("MATCH(u:User{token:{token}})-[m:MESSAGED]-(r:User) "
			+ "WHERE toLower(r.name) starts with {q} OR toLower(r.username) starts with {q} "
			+ "AND NOT (u)-[:BLOCKED]-(r)"
			+ "RETURN m.senId as senId, m.recId as recId, m.txtMessage as txtMessage, "
			+ "m.time as time, m.sharedProfileUserId as sharedProfileUserId, "
			+ "m.SharedProfileUserPicUrl as sharedProfileUserPicUrl, "
			+ "m.sharedProfileUsername as sharedProfileUsername,"
			+ "m.sharedProfileName as sharedProfileName, m.sharedWibeId as sharedWibeId, "
			+ "m.sharedWibeUploaderPicUrl as sharedWibeUploaderPicUrl, "
			+ "m.sharedWibeThumbnail as sharedWibeThumbnail, "
			+ "m.sharedWibeUploaderName as sharedWibeUploaderName, m.chatType as chatType,"
			+ "r.username as recUsername, r.imgUrl as recImgUrl "
			+ "ORDER BY m.time DESC SKIP {offset} LIMIT {limit}")
	List<Map<String, Object>> suggestChats(@Param("token") String token, @Param("limit")
	int limit, @Param("offset") int offset, @Param("q") String q);
	
	@Query("MATCH (n:User) WHERE n.username starts with {name} RETURN count (n)")
	int getUsernameCount(@Param("name") String username);
	
	@Query("Match (n:User{token:{token}}) SET n.version = {v}")
	void updateVersion(@Param("token") String token,@Param("v") String v);
}
