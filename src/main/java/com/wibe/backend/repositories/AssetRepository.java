package com.wibe.backend.repositories;

import java.util.List;
import java.util.Map;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.wibe.backend.entities.models.Asset;
import com.wibe.backend.entities.models.Banner;

@RepositoryRestResource(exported=false)
public interface AssetRepository extends GraphRepository<Asset> {

	Asset findByAssetId(@Param("id") long id);
	
	@Query("MATCH (n:Asset) WHERE n.assetType = {type} and n.live = true "
			+ "RETURN n ORDER BY n.weight")
	List<Asset> findbyAssetType(@Param("type") String type);
	
	@Query("MATCH (n:Asset{assetId:{assetId}}) RETURN n")
	Asset getAssetById(@Param("assetId") long assetId);
	
	@Query("MERGE (n:Banner{lan:{lan}}) SET n.url = {url}, n.tag= {tag}, n.active = {active}, "
			+ "n.duration = {duration}, n.height = {height}, n.width = {width} RETURN n")
	Banner updateBanner(@Param("lan") String lan, @Param("url") String url, @Param("tag")String tag, 
			@Param("active") boolean active,@Param("duration") int duration, @Param("height") int height, 
			@Param("width") int width);
	
	@Query("MATCH (n:Banner{lan:{lan}}) set n.active = {active} RETURN n")
	Banner changeBannerStatus(@Param("lan") String lan, @Param("active") boolean active);
	
	@Query("MATCH (n:Asset{assetId:{assetId}}) set n.live = {status} RETURN n")
	Asset changeAssetStatus(@Param("assetId") long assetId, @Param("status") boolean status);
	
	@Query("MERGE (w:Asset{assetId:{assetId}}) "
			+ "ON CREATE set w = {asset} "
			+ "ON MATCH  set w = {asset} RETURN w")
	Asset update(@Param("assetId") long id, @Param("asset") Map<String, Object> asset);
	
}
