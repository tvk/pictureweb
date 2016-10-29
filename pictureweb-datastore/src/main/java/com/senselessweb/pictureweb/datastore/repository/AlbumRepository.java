package com.senselessweb.pictureweb.datastore.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.senselessweb.pictureweb.datastore.domain.StoredAlbum;

public interface AlbumRepository extends MongoRepository<StoredAlbum, String> {
}
