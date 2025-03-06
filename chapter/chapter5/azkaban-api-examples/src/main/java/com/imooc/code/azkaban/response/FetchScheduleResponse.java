package com.imooc.code.azkaban.response;

import com.imooc.code.azkaban.model.Schedule;
import lombok.Data;

@Data
public class FetchScheduleResponse extends BaseResponse {
    private Schedule schedule;

}