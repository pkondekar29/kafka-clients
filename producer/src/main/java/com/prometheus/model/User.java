package com.prometheus.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private String userId;

    private String name;

    @Override
    public String toString() {
        return "[Id: " + userId + ", name: " + name + "]";
    }

}