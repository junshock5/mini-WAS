package com.nhn.miniwas;

import com.nhn.miniwas.request.HttpRequest;
import com.nhn.miniwas.request.HttpRequestGenerator;
import com.nhn.miniwas.response.HttpResponse;
import com.nhn.miniwas.response.HttpResponseGenerator;
import com.nhn.miniwas.util.HttpRequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

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
        } catch (IOException ex) {
        }
        if (indexFileName != null)
            this.indexFileName = indexFileName;
        this.rootDirectory = rootDirectory;
        this.documentDirectory = documentDirectory;
        this.connection = connection;
    }

    @Override
    public void run() {
        logger.info("NewClientConnect!ConnectedIP:{},Port:{}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequestGenerator.createHttpRequest(in);
            HttpResponse httpResponse = HttpResponseGenerator.createHttpResponse(out);

            sendDirectoryFile(httpRequest, httpResponse);

            // sepc 1. host if progress
            // if(connection.getInetAddress() == "testAdress") {}

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendDirectoryFile(HttpRequest httpRequest, HttpResponse httpResponse) {
        // html 파일 경로
        String documentDirectoryPath = rootDirectory.getPath() + documentDirectory;

        try {
            // 명령어 입력
            Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()), "UTF-8");
            StringBuilder requestLine = new StringBuilder();
            while (true) {
                int c = in.read();
                if (c == '\r' || c == '\n')
                    break;
                requestLine.append((char) c);
            }

            String version = "";
            String fileName = httpRequest.getPath();
            if (httpRequest.getMethod().equals("GET")) {
                logger.info("RemoteSocketAddress {}, {}", connection.getRemoteSocketAddress(), requestLine.toString());

                if (fileName.endsWith(File.separator)) fileName += indexFileName;

                if (httpRequest.getVersion().length() > 2) {
                    version = httpRequest.getVersion();
                }

                File file = new File(documentDirectoryPath, fileName.substring(1, fileName.length()));

                // file.getAbsolutePath() 의 File.separator 개수가 rootDirectory 의 개수가 더 적을 경우 상위 경로 접근, 403 forbidden 처리
                if (HttpRequestUtils.getRootSeparatorCount(file.getAbsolutePath()) < HttpRequestUtils.getRootSeparatorCount(rootDirectory.getAbsolutePath())) {
                    httpResponse.forbidden(fileName);
                }
                // fileName의 확장자명이 .exe 일 경우 403 forbidden 처리
                String[] result = fileName.split("\\.");
                String extension = result[result.length - 1];
                if (extension.equals("exe"))
                    httpResponse.forbidden(fileName);

                if (file.canRead() && file.getCanonicalPath().startsWith(documentDirectoryPath)) {
                    // HTTP 1.1 버전 일때 처리
                    if (version.startsWith("/1.1"))
                        httpResponse.processFile(fileName);

                } else {
                    // not found Directory file
                    httpResponse.notFound(fileName);
                }

            } else {
                // method does not equal "GET"
                httpResponse.error(fileName);
            }
        } catch (IOException ex) {
            logger.error("Level:{} Server could not start RemoteSocketAddress:{}, Exception:{}", Level.WARNING, connection.getRemoteSocketAddress(), ex);
        } finally {
            try {
                connection.close();
            } catch (IOException ex) {
            }
        }
    }

}