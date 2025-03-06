package com.imooc.code.azkaban;

import com.imooc.code.azkaban.response.*;
import org.junit.Test;

import java.io.IOException;

public class AzkabanApiTest {

    AzkabanApi azkabanApi= new AzkabanApiImpl("azkaban","azkaban","http://47.108.140.82:8081");

    @Test
    public void createProject() throws IOException{
        azkabanApi.login();

        BaseResponse response = azkabanApi.createProject("az-api", "az-api test");

        System.out.println("create project " + response.getStatus());
    }

    @Test
    public void uploadZip() throws IOException{
        azkabanApi.login();
        ProjectZipResponse projectZipResponse = azkabanApi.uploadProjectZip("/home/jixin/cmd_test.zip", "az-api");
        System.out.println("upload zip "+projectZipResponse.getStatus());

    }

    @Test
    public void fetchProjectFlows() throws IOException{
        azkabanApi.login();
        FetchFlowsResponse fetchFlowsResponse = azkabanApi.fetchProjectFlows("az-api");
        System.out.println(fetchFlowsResponse.getFlows());
    }

    @Test
    public void deleteProject() throws IOException{
        azkabanApi.login();
        azkabanApi.deleteProject("az-api");
    }

    @Test
    public void execFlow() throws IOException, InterruptedException {
        azkabanApi.login();
        ExecFlowResponse cmd_test = azkabanApi.executeFlow("az-api", "cmd_test");
        System.out.println("execid "+cmd_test.getExecid());
        FetchExecFlowResponse fetchExecFlowResponse = azkabanApi.fetchExecFlow(cmd_test.getExecid());
        System.out.println("exec info " +fetchExecFlowResponse.toString());
        Thread.sleep(1000);
        FetchExecJobLogs jobLogs = azkabanApi.fetchExecJobLogs(cmd_test.getExecid(), "cmd_test", 0, 5000);
        System.out.println("loginfo "+ jobLogs.getData());


    }

    @Test
    public void scheduleFlow() throws IOException{
        azkabanApi.login();
        ScheduleCronFlowResponse cmd_test = azkabanApi.scheduleCronFlow("az-api", "cmd_test", "0 * * * * ? *");
        System.out.println("schedule id" + cmd_test.getScheduleId());


    }

    @Test
    public void removeSchedule() throws IOException{
        azkabanApi.login();
        azkabanApi.removeSchedule("2");
    }
}
