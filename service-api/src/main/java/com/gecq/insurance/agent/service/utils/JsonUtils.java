package com.gecq.insurance.agent.service.utils;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.node.ObjectNode;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Created by gecq on 2016/1/19 0019.
 */
@Component("jsonUtils")
public class JsonUtils {
    private ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    public int toJson(HttpServletResponse httpServletResponse, Object object) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            JsonGenerator jsonGenerator = getObjectMapper().getJsonFactory().createJsonGenerator(out, JsonEncoding.UTF8);
            byte[] resBytes;
            objectMapper.writeValue(jsonGenerator, object);
            jsonGenerator.flush();
            resBytes = out.toByteArray();
            logger.info("response: {}", out.toString());
            httpServletResponse.setHeader("Content-Type", "application/json");
            httpServletResponse.setHeader("Content-Length", String.valueOf(resBytes.length));
            httpServletResponse.setContentLength(resBytes.length);
            httpServletResponse.setCharacterEncoding(JsonEncoding.UTF8.getJavaName());
            ServletOutputStream servletOutputStream = httpServletResponse.getOutputStream();
            PrintStream printStream = new PrintStream(servletOutputStream);
            printStream.write(resBytes);
            printStream.flush();
            printStream.close();
            return resBytes.length;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public String toJson(Object obj){
        try {
            return getObjectMapper().writeValueAsString(obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{}";

    }

    private ObjectMapper getObjectMapper() throws IOException {
        if (this.objectMapper == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            AnnotationIntrospector introspector = new JaxbAnnotationIntrospector();
            SerializationConfig serializationConfig = objectMapper.getSerializationConfig();
            serializationConfig = serializationConfig.withAnnotationIntrospector(introspector);
            objectMapper.setSerializationConfig(serializationConfig);
            this.objectMapper = objectMapper;
        }
        return this.objectMapper;

    }
}
