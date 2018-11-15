package org.petka.sboot.persistence.repositories;

import org.petka.sboot.persistence.documents.MongoAccessToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MongoAccessTokenRepository extends CrudRepository<MongoAccessToken, String> {

    MongoAccessToken findByClientId(String clientId);

    List<MongoAccessToken> findAllByClientId(String clientId);

    MongoAccessToken findByTokenId(String tokenId);

    MongoAccessToken findByRefreshToken(String refreshToken);

    MongoAccessToken findByAuthenticationId(String authenticationId);

    List<MongoAccessToken> findByClientIdAndUsername(String clientId, String username);
}
