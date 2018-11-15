package org.petka.sboot.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.List;

//@Configuration
public class MongoConfig {

    @Autowired
    List<Converter<?, ?>> customConverters;

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(customConverters);
    }
}
