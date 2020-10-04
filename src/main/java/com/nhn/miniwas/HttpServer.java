package com.nhn.miniwas;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public class HttpServer {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(HttpServer.class);
    private static final int NUM_THREADS = 50;
    private static final String INDEX_FILE = File.separator + "index.html";
    private static int port;
    public static JSONArray HtmlStatusCodeArray;
    public static JSONArray Controller_Mapping_List;
    private static String documentDirectory;
    private final File rootDirectory;

    public HttpServer(File rootDirectory) throws IOException {

        if (!rootDirectory.isDirectory()) {
            throw new IOException(rootDirectory + " does not exist as a directory");
        }
        this.rootDirectory = rootDirectory;
    }

    public void start() throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(NUM_THREADS);

        try (ServerSocket server = new ServerSocket(port, 50, InetAddress.getByName("localhost"))) {
            logger.info("HostName:{}, InetAddress:{}, Port:{}, DocumentRoot:{}",
                    InetAddress.getLocalHost().getHostName(), server.getInetAddress(), server.getLocalPort(), rootDirectory);
            while (true) {
                try {
                    Socket connection = server.accept();
                    Runnable r = new RequestHandler(rootDirectory, documentDirectory, INDEX_FILE, connection);
                    pool.submit(r);
                } catch (IOException ex) {
                    logger.error("Level:{} Error accepting connection Exception:{}", Level.WARNING, ex);
                }
            }
        }
    }

    public static void settingJson(String rootDirtory) {
        String jsonDirectory = rootDirtory + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "properties.json";
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(jsonDirectory));

            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.
            JSONObject jsonObject = (JSONObject) obj;

            try {
                port = Integer.parseInt((String) jsonObject.get("Port"));
                if (port < 0 || port > 65535) port = 80;
                documentDirectory = (String) jsonObject.get("Root");
            } catch (RuntimeException ex) {
                port = 80;
                documentDirectory = File.separator + "webapp";
            }

            // A JSON array. JSONObject supports java.util.List interface.
            HtmlStatusCodeArray = (JSONArray) jsonObject.get("Html_Code_State_List");
            Controller_Mapping_List = (JSONArray) jsonObject.get("Controller_Mapping_List");
        } catch (Exception e) {
            logger.error("settingJson {} ", e);
        }
    }

    public static void main(String[] args) {

        File rootDirectory;
        String projectDirectory;

        if (args[0] == null)
            throw new RuntimeException("명령줄에 프로젝트 최상위 경로를 입력해주세요");
        else
            projectDirectory = args[0];

        try {
            rootDirectory = new File(projectDirectory);
            settingJson(projectDirectory);
        } catch (ArrayIndexOutOfBoundsException ex) {
            logger.error("Usage: java JHTTP docroot port {} ", ex);
            return;
        }
        try {
            HttpServer httpServer = new HttpServer(rootDirectory);
            httpServer.start();
        } catch (IOException ex) {
            logger.error("Level:{} Server could not start Exception:{}", Level.SEVERE, ex);
        }
    }
}