package com.prometheus.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class User {

    private String userId;

    private String name;

    @Override
    public String toString() {
        return "[Id: " + userId + ", name: " + name + "]";
    }

}