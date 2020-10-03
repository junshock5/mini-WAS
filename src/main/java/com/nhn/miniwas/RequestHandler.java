package com.nhn.miniwas;

import com.nhn.miniwas.request.HttpRequest;
import com.nhn.miniwas.request.HttpRequestGenerator;
import com.nhn.miniwas.response.HttpResponse;
import com.nhn.miniwas.response.HttpResponseGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
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

            sendDriectoryFile(httpResponse, out);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void sendHeader(Writer out, String responseCode, String contentType, int length)
            throws IOException {
        out.write(responseCode + "\r\n");
        Date now = new Date();
        out.write("Date: " + now + "\r\n");
        out.write("Server: JHTTP 2.0\r\n");
        out.write("Content-length: " + length + "\r\n");
        out.write("Content-type: " + contentType + "\r\n\r\n");
        out.flush();
    }

    private void sendDriectoryFile(HttpResponse httpResponse, OutputStream outputStream) {
        String root = rootDirectory.getPath();
        try {
            //OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
            Writer out = new OutputStreamWriter(outputStream);
            Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()), "UTF-8");
            StringBuilder requestLine = new StringBuilder();
            while (true) {
                int c = in.read();
                if (c == '\r' || c == '\n')
                    break;
                requestLine.append((char) c);
            }
            String get = requestLine.toString();

            String[] tokens = get.split("\\s+");
            String method = tokens[0];
            String version = "";
            if (method.equals("GET")) {
                logger.info("RemoteSocketAddress {}, {}", connection.getRemoteSocketAddress(), get);
                String fileName = tokens[1];
                if (fileName.endsWith("/")) fileName += indexFileName;
                String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
                if (tokens.length > 2) {
                    version = tokens[2];
                }
                File theFile = new File(rootDirectory, fileName.substring(1, fileName.length()));
                if (theFile.canRead() && theFile.getCanonicalPath().startsWith(root)) {
                    byte[] theData = Files.readAllBytes(theFile.toPath());
                    if (version.startsWith("HTTP/")) {
                        sendHeader(out, "HTTP/1.1 200 OK", contentType, theData.length);
                    }
                    httpResponse.processFile(fileName);
                } else {
                    if (version.startsWith("HTTP/")) { // send a MIME header
                        //sendHeader(out, "HTTP/1.1 404 File Not Found", "text/html; charset=utf-8", body.length());
                    }
                    httpResponse.notFound("/index.html");
                }
            } else {
                // method does not equal "GET"
                if (version.startsWith("HTTP/")) { // send a MIME header
                    //sendHeader(out, "HTTP/1.1 501 Not Implemented", "text/html; charset=utf-8", body.length());
                }
                httpResponse.error("/index.html");
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