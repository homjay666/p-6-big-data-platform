package com.imooc.code.azkaban.response;

import lombok.Data;

@Data
public class ScheduleCronFlowResponse extends BaseResponse {
    private String scheduleId;
}
