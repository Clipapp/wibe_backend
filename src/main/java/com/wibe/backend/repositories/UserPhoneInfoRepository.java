package com.wibe.backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.wibe.backend.requests.UserPhoneInfo;

@RepositoryRestResource(exported=false)
@Repository
public interface UserPhoneInfoRepository extends MongoRepository<UserPhoneInfo, String> {

}
