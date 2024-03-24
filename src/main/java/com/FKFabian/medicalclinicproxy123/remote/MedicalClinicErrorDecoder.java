package com.FKFabian.medicalclinicproxy123.remote;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

import java.util.Date;

public class MedicalClinicErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder defaultError = new ErrorDecoder.Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        FeignException exception = feign.FeignException.errorStatus(methodKey, response);
        if (response.status() == 503) {
            return new RetryableException(
                    response.status(),
                    exception.getMessage(),
                    response.request().httpMethod(),
                    exception,
                    (Date) null,
                    response.request());
        }
        return defaultError.decode(methodKey, response);
    }
}
