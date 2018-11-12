package org.petka.sboot.persistence.convertes;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.petka.sboot.persistence.documents.MongoAccessToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenConverter implements Converter<MongoAccessToken, DBObject> {

    @Override
    public DBObject convert(MongoAccessToken source) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("value", source.getToken().getValue());
        dbObject.put("expiration", source.getToken().getExpiration());
        dbObject.put("tokenType", source.getToken().getTokenType());
        dbObject.removeField("_class");

        return dbObject;
    }
}
