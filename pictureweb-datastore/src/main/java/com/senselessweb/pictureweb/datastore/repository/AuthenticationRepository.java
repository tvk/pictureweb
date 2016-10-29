package com.senselessweb.pictureweb.datastore.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.senselessweb.pictureweb.datastore.domain.StoredAuthentication;

public interface AuthenticationRepository extends MongoRepository<StoredAuthentication, String> {

}
