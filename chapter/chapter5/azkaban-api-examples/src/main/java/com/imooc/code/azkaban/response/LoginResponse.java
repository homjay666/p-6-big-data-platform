package com.imooc.code.azkaban.response;

import lombok.Data;

@Data
public class LoginResponse extends BaseResponse {
    private String sessionId;
}
