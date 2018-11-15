package org.petka.sboot.persistence.repositories;

import org.petka.sboot.persistence.documents.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    RefreshToken findByTokenId(String tokenId);
}

