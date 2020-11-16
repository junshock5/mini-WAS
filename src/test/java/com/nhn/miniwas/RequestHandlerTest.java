package com.nhn.miniwas;

import com.nhn.miniwas.util.HttpRequestUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;

import static org.junit.Assert.*;

public class RequestHandlerTest {
    private String fileDirectory;
    private String fileName;
    private String rootDirectory;

    @Before
    public void setUp() throws Exception {
        fileDirectory = "C:\\Users\\topojs8\\Downloads\\mini-was\\webapp\\index.html";
        fileName = "index.html";

        String jsonDirectory = ".\\src\\main\\resources\\properties.json";
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(jsonDirectory));
        JSONObject jsonObject = (JSONObject) obj;
        rootDirectory = (String) jsonObject.get("Root");
    }

    @Test
    public void checkUpperDirectoryFileExtension() {
        File file = new File(fileDirectory, fileName.substring(1, fileName.length()));

        // file.getAbsolutePath() 의 File.separator 개수가 rootDirectory 의 개수가 더 적을 경우 상위 경로 접근
        boolean isUpper = false;
        if (HttpRequestUtils.getRootSeparatorCount(file.getAbsolutePath()) <
                HttpRequestUtils.getRootSeparatorCount(rootDirectory))
            isUpper = true;
        assertEquals(isUpper, false);

        // fileName의 확장자명이 .exe 인지 확인
        String[] result = fileName.split("\\.");
        String extension = result[result.length - 1];
        assertFalse("exe".equals(extension));
    }

}