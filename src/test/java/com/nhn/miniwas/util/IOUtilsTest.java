package com.nhn.miniwas.util;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;


public class IOUtilsTest {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(IOUtilsTest.class);

    @Test
    public void readData() throws IOException {
        String data = "testData";
        StringReader sr = new StringReader(data);
        BufferedReader br = new BufferedReader(sr);

        logger.info("parse body : {}", IOUtils.readData(br, data.length()));
    }
}