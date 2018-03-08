package com.wibe.backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import com.wibe.backend.requests.Contacts;

@RepositoryRestResource(exported=false)
@Repository
public interface ContactRepository extends MongoRepository<Contacts, String>{

}
