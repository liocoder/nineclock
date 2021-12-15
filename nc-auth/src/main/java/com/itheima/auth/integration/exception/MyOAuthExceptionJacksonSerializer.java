package com.itheima.auth.integration.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
* @Description 定义异常MyOAuth2Exception的序列化
* @Param
* @Return
*/
public class MyOAuthExceptionJacksonSerializer extends StdSerializer<MyOAuth2Exception> {

    protected MyOAuthExceptionJacksonSerializer() {
        super(MyOAuth2Exception.class);
    }

    @Override
    public void serialize(MyOAuth2Exception value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("success", false);
        jgen.writeObjectField("code", value.getHttpErrorCode());
        jgen.writeStringField("message", value.getMessage());
        jgen.writeEndObject();
    }
}
