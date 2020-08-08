package com.prometheus.kafka.consumer.utils;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Recorder extends ConcurrentHashMap<Long, Integer> {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    
    private static Recorder map = null;

    private Recorder() {

    }

    public synchronized static Recorder getInstance() {
        if(map == null) {
            map = new Recorder();
        }
        return map;
    }

}