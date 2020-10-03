package com.nhn.miniwas;

import com.nhn.miniwas.request.HttpRequest;
import com.nhn.miniwas.request.HttpRequestGenerator;
import com.nhn.miniwas.response.HttpResponse;
import com.nhn.miniwas.response.HttpResponseGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;

public class RequestHandler implements Runnable {
    private final static Logger logger = (Logger) LoggerFactory.getLogger(RequestHandler.class);
    private File rootDirectory;
    private String indexFileName;
    private Socket connection;

    public RequestHandler(File rootDirectory, String indexFileName, Socket connection) {
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
        this.connection = connection;
    }

    @Override
    public void run() {
        logger.info("NewClientConnect!ConnectedIP:{},Port:{}", connection.getInetAddress(), connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            HttpRequest httpRequest = HttpRequestGenerator.createHttpRequest(in);
            HttpResponse httpResponse = HttpResponseGenerator.createHttpResponse(out);

            sendDriectoryFile(httpRequest, httpResponse, out);

            // sepc 1. host if progress
            // if(connection.getInetAddress() == "testAdress") {}

        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendDriectoryFile(HttpRequest httpRequest, HttpResponse httpResponse, OutputStream outputStream) {
        String root = rootDirectory.getPath();
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

                if (fileName.endsWith("/")) fileName += indexFileName;

                if (httpRequest.getVersion().length() > 2) {
                    version = httpRequest.getVersion();
                }

                File file = new File(rootDirectory, fileName.substring(1, fileName.length()));

                if (file.canRead() && file.getCanonicalPath().startsWith(root)) {
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