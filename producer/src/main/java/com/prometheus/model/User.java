package com.prometheus.model;

import java.io.Serializable;

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
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2653291604375869148L;

    private String userId;

    private String name;

    @Override
    public String toString() {
        return "[Id: " + userId + ", name: " + name + "]";
    }

}