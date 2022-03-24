package com.swivel.cc.auth.configuration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.swivel.cc.auth.enums.ErrorResponseStatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * This Exception serializer is used to return a custom error response for Invalid credentials
 * This will override the bad credentials error thrown from InvalidGrantException
 */
@Component
public class CustomInvalidLoginExceptionSerializer extends StdSerializer<CustomLoginException> {

    private transient Translator translator;

    public CustomInvalidLoginExceptionSerializer() {
        super(CustomLoginException.class);
    }

    @Autowired
    public CustomInvalidLoginExceptionSerializer(Translator translator) {
        super(CustomLoginException.class);
        this.translator = translator;
    }

    @Override
    public void serialize(CustomLoginException e, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("errorCode", ErrorResponseStatusType.INVALID_LOGIN.getCode());
        jsonGenerator.writeStringField("status", "ERROR");
        jsonGenerator.writeObjectField("data", null);
        jsonGenerator.writeObjectField("message", ErrorResponseStatusType.INVALID_LOGIN.getMessage());
        jsonGenerator.writeObjectField("displayMessage", translator.toLocale(ErrorResponseStatusType
                .INVALID_LOGIN.getCodeString(ErrorResponseStatusType.INVALID_LOGIN.getCode())));
        jsonGenerator.writeEndObject();
    }

}
