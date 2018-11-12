package org.petka.sboot.persistence.repositories;

import org.petka.sboot.persistence.documents.MongoAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface MongoAccessTokenRepository extends CrudRepository<MongoAccessToken, String> {

    MongoAccessToken findByClientId(String clientId);
    MongoAccessToken findByTokenId(String tokenId);
}
