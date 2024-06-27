package com.blogex.api.config;

import io.jsonwebtoken.Jwt;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;
import java.util.List;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "zzamuni")
public class AppConfig {

//    public static final String KEY = "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIn0.JHfb7QmpG6jnYdB_Ou4rf-nnVDeZj4GSpBcHun1GKHXzEHNiZCWu8dOGWxzP0v_8";

//      public Map<String, String> hello;

    public byte[] jwtKey;

    // jwtKey decode
    public void setJwtKey(String jwtKey){
        this.jwtKey = Base64.getDecoder().decode(jwtKey);
    }

    public byte[] getJwtKey() {
        return jwtKey;
    }

    //    @Data
//    public static class Hello{
//        public String name;
//        public String home;
//        public String hobby;
//        public Long age;
//    }
}

