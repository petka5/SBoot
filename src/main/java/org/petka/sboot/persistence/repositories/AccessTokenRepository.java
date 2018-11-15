package org.petka.sboot.persistence.repositories;

import org.petka.sboot.persistence.documents.AccessToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {

    AccessToken findByClientId(String clientId);

    List<AccessToken> findAllByClientId(String clientId);

    AccessToken findByTokenId(String tokenId);

    AccessToken findByRefreshToken(String refreshToken);

    AccessToken findByAuthenticationId(String authenticationId);

    List<AccessToken> findByClientIdAndUsername(String clientId, String username);
}
