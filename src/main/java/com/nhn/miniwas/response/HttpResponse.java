package com.nhn.miniwas.response;

import com.nhn.miniwas.HttpServer;
import com.nhn.miniwas.util.HttpResponseUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpResponse {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(HttpResponse.class);
    private StatusLine statusLine;
    private ResponseHeader responseHeader;
    private ResponseBody responseBody;
    private DataOutputStream dataOutputStream;

    public HttpResponse(DataOutputStream dataOutputStream) {
        this.dataOutputStream = dataOutputStream;
        responseHeader = new ResponseHeader();
    }

    private void addStatusLine(String httpVersion, HttpStatusCode statusCode) {
        statusLine = new StatusLine(httpVersion, statusCode);
    }

    private void addResponseBody(String url) {
        try {
            this.responseBody = new ResponseBody(Files.readAllBytes(Paths.get("./webapp" + url)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addResponseDynamicBody(String dynamicResource) {
        this.responseBody = new ResponseBody(dynamicResource.getBytes());
    }

    public void sendRedirect(String url) {
        addStatusLine(HttpResponseUtils.HTTP_VERSION_1_1, HttpStatusCode.FOUND);
        addHeader("Location", url);
        writeResponseMessage();
    }

    public void forbidden(String url) {
        addStatusLine(HttpResponseUtils.HTTP_VERSION_1_1, HttpStatusCode.FORBIDDEN);
        addHeader("Location", url);
        addResponseDynamicBody(makeHtmlByCode(HttpStatusCode.FORBIDDEN.getHttpStatusNumber().substring(0, 3)));
        writeResponseMessage();
        responseBody.responseBody(dataOutputStream);
    }

    public void notFound(String url) {
        addStatusLine(HttpResponseUtils.HTTP_VERSION_1_1, HttpStatusCode.NOT_FOUND);
        addHeader("Location", url);
        addResponseDynamicBody(makeHtmlByCode(HttpStatusCode.NOT_FOUND.getHttpStatusNumber().substring(0, 3)));
        writeResponseMessage();
        responseBody.responseBody(dataOutputStream);
    }

    public void error(String url) {
        addStatusLine(HttpResponseUtils.HTTP_VERSION_1_1, HttpStatusCode.ERROR);
        addHeader("Location", url);
        addResponseDynamicBody(makeHtmlByCode(HttpStatusCode.ERROR.getHttpStatusNumber().substring(0, 3)));
        writeResponseMessage();
        responseBody.responseBody(dataOutputStream);
    }

    private void writeResponseMessage() {
        try {
            statusLine.addWriteStatusLine(dataOutputStream);
            responseHeader.addWriteHeader(dataOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void process(String filePath) {
        addStatusLine(HttpResponseUtils.HTTP_VERSION_1_1, HttpStatusCode.OK);
        logger.info("filePath : {}", filePath);
        addResponseBody(filePath);

        addHeader("Content-Length", responseBody.getBodyLength());
        writeResponseMessage();
        responseBody.responseBody(dataOutputStream);
    }

    public void processBody(String dynamicResource) {
        addStatusLine(HttpResponseUtils.HTTP_VERSION_1_1, HttpStatusCode.OK);
        addResponseDynamicBody(dynamicResource);
        addHeader("Content-Length", responseBody.getBodyLength());
        writeResponseMessage();
        responseBody.responseBody(dataOutputStream);
    }

    public void addHeader(String header, String value) {
        responseHeader.addHeader(header, value);
    }

    public String makeHtmlByCode(String HttpCode) {
        String JsonValue = "";
        String result = "";
        if (HttpServer.HtmlStatusCodeArray != null)
            for (int i = 0; i < HttpServer.HtmlStatusCodeArray.size(); i++) {
                JSONObject jsonObj2 = (JSONObject) HttpServer.HtmlStatusCodeArray.get(i);
                if ((String) jsonObj2.get(HttpCode) != null) {
                    JsonValue = (String) jsonObj2.get(HttpCode);
                    result = "<HTML>\r\n" +
                            "<HEAD><TITLE> " + JsonValue + " </TITLE>\r\n" +
                            "</HEAD>\r\n" +
                            "<BODY>\r\n" +
                            "<H1>HTTP Error " + HttpCode + " " + JsonValue + " </H1>\r\n" +
                            "</BODY>\r\n" +
                            "</HTML>\r\n";
                    return result;
                }

            }
        return result;
    }
}
