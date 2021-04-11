package com.prometheus.enums;

import java.util.Random;

public enum OrderStatus {

    ORDER_PLACED,
    ORDER_SHIPPED,
    ORDER_RECEIVED,
    ORDER_CANCELLED,
    ORDER_PACKED;

    public static OrderStatus getRandom() {
        Random random = new Random();
        switch(random.nextInt(5)) {
            case 1: return ORDER_PLACED;
            case 2: return ORDER_SHIPPED;
            case 3: return ORDER_RECEIVED;
            case 4: return ORDER_CANCELLED;
            default: return ORDER_PACKED;
        }
    }

}
