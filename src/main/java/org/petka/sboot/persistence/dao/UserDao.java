package org.petka.sboot.persistence.dao;


import org.bson.types.ObjectId;
import org.petka.sboot.persistence.documents.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface UserDao extends CrudRepository<User, ObjectId> {

    @Query("{ 'username': :#{#username}, 'status': 'ENABLED'}")
    User findByUsername(@Param("username") String username);
}
