package com.nhn.miniwas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class HttpServer {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(HttpServer.class);
    private static final int NUM_THREADS = 50;
    private static final String INDEX_FILE = File.separator + "index.html";
    private final File rootDirectory;
    private final int port;

    public HttpServer(File rootDirectory, int port) throws IOException {
        if (!rootDirectory.isDirectory()) {
            throw new IOException(rootDirectory + " does not exist as a directory");
        }
        this.rootDirectory = rootDirectory;
        this.port = port;
    }

    public void start() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);

        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName("localhost"))) {
            logger.info("HostName:{}, InetAddress:{}, Port:{}, DocumentRoot:{}",
                    InetAddress.getLocalHost().getHostName(), server.getInetAddress(), server.getLocalPort(), rootDirectory);
            while (true) {
                try {
                    Socket connection = server.accept();
                    Runnable r = new RequestHandler(rootDirectory, INDEX_FILE, connection);
                    pool.submit(r);
                } catch (IOException ex) {
                    logger.error("Level:{} Error accepting connection Exception:{}", Level.WARNING, ex);
                }
            }
        }
    }

    public static void main(String[] args) {
        File docroot;
        try {
            docroot = new File(args[0]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            System.out.println("Usage: java JHTTP docroot port");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(args[1]);
            if (port < 0 || port > 65535) port = 80;
        } catch (RuntimeException ex) {
            port = 80;
        }
        try {
            HttpServer webserver = new HttpServer(docroot, port);
            webserver.start();
        } catch (IOException ex) {
            logger.error("Level:{} Server could not start Exception:{}", Level.SEVERE, ex);
        }
    }
}