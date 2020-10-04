package com.nhn.miniwas;

import com.nhn.miniwas.controller.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HandlerMapping {
    private static Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("/index", new HomeController());
        if (HttpServer.Controller_Mapping_List != null) {
            Iterator<String> iterator = HttpServer.Controller_Mapping_List.iterator();
            while (iterator.hasNext()) {
                controllers.put("/" + iterator.next(), new TimeController());
                controllers.put("/service." + iterator.next(), new TimeController());
            }
        }

    }

    static Controller findController(String url) {
        for (String key : controllers.keySet()) {
            if (url.startsWith(key)) {
                return controllers.get(key);
            }
        }
        return new DefaultController();
    }
}
