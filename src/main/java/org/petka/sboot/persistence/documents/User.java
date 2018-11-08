package org.petka.sboot.persistence.documents;

import org.bson.types.ObjectId;
import org.petka.sboot.utils.Constants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Document(collection = "users")
public class User {
    @Id
    private ObjectId id;

    @Indexed
    private String username;

    private String password;

    private Constants.UserStatus status;

    @DateTimeFormat(style = "M-")
    @CreatedDate
    private Date createdDate;


    @LastModifiedDate
    private Date lastModifiedDate;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
