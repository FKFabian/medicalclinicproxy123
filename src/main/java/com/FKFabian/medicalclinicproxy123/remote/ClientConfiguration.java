package com.FKFabian.medicalclinicproxy123.remote;

import feign.Retryer;
import feign.codec.ErrorDecoder;
import feign.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ClientConfiguration {
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(5L), 3);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new MedicalClinicErrorDecoder();
    }

    @Bean
    public OkHttpClient client() {
        return new OkHttpClient();
    }
}
