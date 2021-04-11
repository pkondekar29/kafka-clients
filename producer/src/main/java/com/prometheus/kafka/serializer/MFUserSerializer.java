package com.prometheus.kafka.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prometheus.model.MFOrderEvent;
import org.apache.kafka.common.serialization.Serializer;

public class MFUserSerializer implements Serializer<MFOrderEvent> {

    @Override
    public byte[] serialize(String topic, MFOrderEvent data) {
        byte[] retVal = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            retVal = objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

}
