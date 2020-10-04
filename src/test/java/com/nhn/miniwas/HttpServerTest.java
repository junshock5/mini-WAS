package com.nhn.miniwas;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class HttpServerTest {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(HttpServerTest.class);

    @Test
    public void settingJson() {
        String jsonDirectory = ".\\src\\main\\resources\\properties.json";
        JSONParser parser = new JSONParser();
        int port;
        String root;
        try {
            Object obj = parser.parse(new FileReader(jsonDirectory));
            JSONObject jsonObject = (JSONObject) obj;

            port = Integer.parseInt((String) jsonObject.get("Port"));
            assertThat(port, is(80));

            root = (String) jsonObject.get("Root");
            assertThat(root, is("\\src\\main\\resources\\webapp"));

        } catch (Exception e) {
            logger.error("settingJson {} ", e);
        }

    }
}