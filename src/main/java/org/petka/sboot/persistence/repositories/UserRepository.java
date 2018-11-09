package org.petka.sboot.persistence.repositories;


import org.bson.types.ObjectId;
import org.petka.sboot.persistence.documents.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends CrudRepository<User, ObjectId> {

    @Query("{ 'username': :#{#username}, 'status': 'ENABLED'}")
    User findByUsername(@Param("username") String username);
}
