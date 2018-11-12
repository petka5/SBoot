package org.petka.sboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoConfig {

    @Autowired
    List<Converter<?,?>> customConverters;

    @Bean
    public MongoCustomConversions customConversions(){
        //List<Converter<?,?>> converters = new ArrayList<>();
        //converters.add(ReferenceWriterConverter.INSTANCE);
        //return new MongoCustomConversions(converters);
        return new MongoCustomConversions(customConverters);

    }
}
