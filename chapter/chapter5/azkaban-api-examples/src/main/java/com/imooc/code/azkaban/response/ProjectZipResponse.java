package com.imooc.code.azkaban.response;

import lombok.Data;

@Data
public class ProjectZipResponse extends BaseResponse {
    private String projectId;
    private String version;
}
