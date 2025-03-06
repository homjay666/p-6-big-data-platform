package com.imooc.code.azkaban;


import com.imooc.code.azkaban.response.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface AzkabanApi {

    //登录

    LoginResponse login() throws IOException;

    //创建Project

    BaseResponse createProject(String name, String desc);

    //上传zip包

    ProjectZipResponse uploadProjectZip(String filePath, String projectName);

    //获取Project下的flows

    FetchFlowsResponse fetchProjectFlows(String projectName);

    //删除Project

    BaseResponse deleteProject(String name);

    //执行flow

    ExecFlowResponse executeFlow(String projectName, String flowName);

    //获取flow执行结果

    FetchExecFlowResponse fetchExecFlow(String execId);

    //获取flow的执行日志

    FetchExecJobLogs fetchExecJobLogs(String execId, String jobId, int offset, int length);

    //调度flow

    ScheduleCronFlowResponse scheduleCronFlow(String projectName, String flowName, String cronExpression) throws UnsupportedEncodingException;


    //删除调度
    BaseResponse removeSchedule(String scheduleId);

    BaseResponse cancelFlow(String execId);
}
