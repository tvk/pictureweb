package com.senselessweb.pictureweb.datastore.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.senselessweb.pictureweb.datastore.domain.StoredPhoto;

public interface PhotoRepository extends MongoRepository<StoredPhoto, String> {
}
