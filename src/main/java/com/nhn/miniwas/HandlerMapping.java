package com.nhn.miniwas;

import com.nhn.miniwas.controller.*;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private static Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("/index", new HomeController());
        controllers.put("/Hello", new HelloController());
        controllers.put("/service.Hello", new HelloController());
        controllers.put("/Time", new TimeController());
        controllers.put("/service.Time", new TimeController());
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
