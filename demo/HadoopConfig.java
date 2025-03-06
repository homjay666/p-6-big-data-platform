package com.example.hadoopweb.config;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@org.springframework.context.annotation.Configuration
public class HadoopConfig {

    @Value("${hadoop.namenode-url}")
    private String namenodeUrl;
    
    @Value("${hadoop.username}")
    private String username;

    @Bean
    public Configuration hadoopConfiguration() {
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS", namenodeUrl);
        return configuration;
    }

    @Bean
    public FileSystem fileSystem() throws IOException, URISyntaxException {
        return FileSystem.get(new URI(namenodeUrl), hadoopConfiguration(), username);
    }
}