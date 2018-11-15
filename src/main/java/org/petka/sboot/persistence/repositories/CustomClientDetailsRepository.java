package org.petka.sboot.persistence.repositories;

import org.petka.sboot.persistence.documents.CustomClientDetails;
import org.springframework.data.repository.CrudRepository;

public interface CustomClientDetailsRepository extends CrudRepository<CustomClientDetails, String> {

    CustomClientDetails findByClientId(String clientId);

}
