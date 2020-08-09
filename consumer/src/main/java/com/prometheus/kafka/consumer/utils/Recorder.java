package com.prometheus.kafka.consumer.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;

public class Recorder extends ConcurrentHashMap<Long, LongAdder> {

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