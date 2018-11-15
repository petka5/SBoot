package org.petka.sboot.persistence.repositories;

import org.petka.sboot.persistence.documents.MongoRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface MongoRefreshTokenRepository extends CrudRepository<MongoRefreshToken, String> {

    MongoRefreshToken findByTokenId(String tokenId);
}

