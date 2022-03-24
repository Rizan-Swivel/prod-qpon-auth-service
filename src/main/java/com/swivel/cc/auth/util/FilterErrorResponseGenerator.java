package com.swivel.cc.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swivel.cc.auth.configuration.Translator;
import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import com.swivel.cc.auth.wrapper.ErrorResponseWrapper;
import com.swivel.cc.auth.wrapper.ResponseWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class FilterErrorResponseGenerator {

    private final Translator translator;

    @Autowired
    public FilterErrorResponseGenerator(Translator translator) {
        this.translator = translator;
    }

    /**
     * Build error response
     *
     * @param response response
     * @param type     error type
     * @throws IOException IOException
     */
    public void sendErrorResponse(HttpServletResponse response, ErrorResponseStatusType type,
                                  HttpStatus httpStatus) throws IOException {
        ErrorResponseWrapper errorResponseWrapper = new ErrorResponseWrapper(type,
                translator.toLocale(type.getCodeString(type.getCode())), null);

        byte[] responseToSend = restResponseBytes(errorResponseWrapper);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(httpStatus.value());
        response.getOutputStream().write(responseToSend);
    }

    /**
     * Generate a response
     *
     * @param responseWrapper responseWrapper
     * @return response
     * @throws IOException IOException
     */
    private byte[] restResponseBytes(ResponseWrapper responseWrapper) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(responseWrapper);
        return serialized.getBytes();
    }

}
