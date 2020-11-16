package com.nhn.miniwas;

import com.nhn.miniwas.controller.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HandlerMapping {
    private static Map<String, Controller> controllers = new HashMap<>();
    private static DefaultController defaultController = new DefaultController();

    static {
        controllers.put("/index", new HomeController());

        // spec 6. json Controller_Mapping_List 값에 따라 추가 맵핑 됩니다.
        if (HttpServer.Controller_Mapping_List != null) {
            Iterator<String> iterator = HttpServer.Controller_Mapping_List.iterator();
            while (iterator.hasNext()) {
                controllers.put("/" + iterator.next(), new TimeController());
            }
        }

    }

    static Controller findController(String url) {
        for (String key : controllers.keySet()) {
            if (url.startsWith(key)) {
                return controllers.get(key);
            }
        }
        return defaultController;
    }
}
