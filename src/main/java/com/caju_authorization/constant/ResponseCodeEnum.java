package com.caju_authorization.constant;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum {

    APPROVED("01"),
    INSUFFICIENT_FUNDS("51"),
    ERROR("07");

    private final String code;

}
