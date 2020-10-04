package com.nhn.miniwas;

import com.nhn.miniwas.request.HttpRequest;
import com.nhn.miniwas.request.HttpRequestGenerator;
import com.nhn.miniwas.response.HttpResponse;
import com.nhn.miniwas.response.HttpResponseGenerator;
import com.nhn.miniwas.util.HttpRequestUtils;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class RequestHandler implements Runnable {
    private final static Logger logger = (Logger) LoggerFactory.getLogger(RequestHandler.class);
    private File rootDirectory;
    private String documentDirectory;
    private String indexFileName;
    private Socket connection;

    public RequestHandler(File rootDirectory, String documentDirectory, String indexFileName, Socket connection) {
        if (rootDirectory.isFile()) {
            throw new IllegalArgumentException("rootDirectory must be a directory, not a file");
        }

        try {
            rootDirectory = rootDirectory.getCanonicalFile();
        } catch (IOException e) {
            logger.error("RequestHandler init {} ", e);
        }
        if (indexFileName != null)
            this.indexFileName = indexFileName;
        this.rootDirectory = rootDirectory;
        this.documentDirectory = documentDirectory;
        this.connection = connection;
    }

    @Override
    public void run() {
        logger.debug("NewClientConnect!ConnectedIP:{},Port:{}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequestGenerator.createHttpRequest(in);
            HttpResponse httpResponse = HttpResponseGenerator.createHttpResponse(out);

            checkUpperDirectoryFileExtension(httpRequest, httpResponse);

            // spec 1. host if progress
            // if(connection.getInetAddress() == "testAdress") {}

            Dispatcher dispatcher = new Dispatcher(httpRequest, httpResponse);
            dispatcher.dispatch();

        } catch (IOException e) {
            logger.error(String.valueOf(e.getStackTrace()));
        }
    }

    private void checkUpperDirectoryFileExtension(HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        logger.info("RemoteSocketAddress {}", connection.getRemoteSocketAddress());

        // html 파일 경로 \webapp 추가.
        String documentDirectoryPath = rootDirectory.getPath() + documentDirectory;
        String fileName = httpRequest.getPath();

        if ("GET".equals(httpRequest.getMethod())) {
            if (fileName.endsWith(File.separator)) fileName += indexFileName;
            File file = new File(documentDirectoryPath, fileName.substring(1, fileName.length()));

            // spec 4. file.getAbsolutePath() 의 File.separator 개수가 rootDirectory 의 개수가 더 적을 경우 상위 경로 접근, 403 forbidden 처리
            if (HttpRequestUtils.getRootSeparatorCount(file.getAbsolutePath()) < HttpRequestUtils.getRootSeparatorCount(rootDirectory.getAbsolutePath())) {
                httpResponse.forbidden(fileName);
            }

            // spec 4. fileName의 확장자명이 .exe 일 경우 403 forbidden 처리
            String[] result = fileName.split("\\.");
            String extension = result[result.length - 1];

            if ("exe".equals(extension))
                httpResponse.forbidden(fileName);

            if (file.canRead() && file.getCanonicalPath().startsWith(documentDirectoryPath)) {
                // 정상 처리 구간 이지만 Dispatcher 가 처리 한다
            } else {
                // not found Directory file
                httpResponse.notFound(fileName);
            }
        } else {
            // method does not equal "GET"
            httpResponse.error(fileName);
        }

    }
}