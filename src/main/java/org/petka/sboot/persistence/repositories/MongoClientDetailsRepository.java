package org.petka.sboot.persistence.repositories;

import org.petka.sboot.persistence.documents.MongoClientDetails;
import org.springframework.data.repository.CrudRepository;

public interface MongoClientDetailsRepository extends CrudRepository<MongoClientDetails, String> {

    MongoClientDetails findByClientId(String clientId);

}
