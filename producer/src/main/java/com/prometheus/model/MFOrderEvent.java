package com.prometheus.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MFOrderEvent {

    private String userName;
    private String orderStatus;
    private String companyName;
    private String productName;
    private Integer quantity;
    private String orderId;

}
