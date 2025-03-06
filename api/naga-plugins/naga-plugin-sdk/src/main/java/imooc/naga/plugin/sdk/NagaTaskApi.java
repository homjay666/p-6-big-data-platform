package imooc.naga.plugin.sdk;

import cn.hutool.http.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NagaTaskApi {
    private String server;
    private int port;

    public static void main(String[] args) throws IOException {
        NagaTaskApi nagaTaskApi = new NagaTaskApi("127.0.0.1", "9042");
//        NagaTaskApi nagaTaskApi = new NagaTaskApi("47.108.140.82", "9042");

        Map<String, Object> jobConfig = nagaTaskApi.getJobConfig("nagatest11", "nagajava", "95");
        System.out.println(jobConfig);
//        Map<String,Object> params = new HashMap<>();
//        params.put("taskName","NagaJavaTask1");
//        params.put("jobName","nagajava");
//        String json = HttpUtil.get("http://47.108.140.82:9042/naga/v1/param/job", params);
//        Map map = JsonUtil.fromJson(Map.class, json);
//        System.out.println(map);

        Map<String,Object> output= new HashMap<>();
        output.put("a",1);
        output.put("b","b");
        nagaTaskApi.saveOutputParams("nagatest11","nagajava","95",output);

        Map<String, Object> jobOutputParams = nagaTaskApi.getJobOutputParams("NagaJavaTask1", "nagajava", "95");
        System.out.println(jobOutputParams);
    }

    public NagaTaskApi(String apiServer, String port) {
        this.server = apiServer;
        this.port = Integer.parseInt(port);

    }

    public Map<String, Object> getJobConfig(String taskName, String jobName, String execId)
            throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("taskName", taskName);
        params.put("jobName", jobName);
        params.put("execId", execId);
        String json = HttpUtil.get(String.format("http://%s:%s/naga/v1/param/job", server, port), params);
        return JsonUtil.fromJson(Map.class, json);
    }

    public void saveJobRuntimeConfig(String taskName, String jobName, String execId,
                                     Map<String, Object> configMap) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("taskName", taskName);
        params.put("jobName", jobName);
        params.put("execId", execId);
        params.put("config",configMap);
        HttpUtil.post(String.format("http://%s:%s/naga/v1/param/runtime", server, port), JsonUtil.toJson(params));
    }

    public void saveOutputParams(String taskName, String jobName, String execId,
                                 Map<String, Object> configMap) throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("taskName", taskName);
        params.put("jobName", jobName);
        params.put("execId", execId);
        params.put("config",configMap);
        HttpUtil.post(String.format("http://%s:%s/naga/v1/param/output", server, port), JsonUtil.toJson(params));

    }

    public Map<String, Map<String, Object>> getTaskOutputParams(String taskName, String execId)
            throws IOException {

        Map<String, Object> params = new HashMap<>();
        params.put("taskName", taskName);
        params.put("execId", execId);
        String json = HttpUtil.get(String.format("http://%s:%s/naga/v1/param/output/task", server, port), params);
        return JsonUtil.fromJson(Map.class, json);
    }

    public Map<String, Object> getJobOutputParams(String taskName, String jobName, String execId)
            throws IOException {

        Map<String, Object> params = new HashMap<>();
        params.put("taskName", taskName);
        params.put("jobName", jobName);
        params.put("execId", execId);
        String json = HttpUtil.get(String.format("http://%s:%s/naga/v1/param/output/job", server, port), params);
        return JsonUtil.fromJson(Map.class, json);
    }

    public boolean saveTableInfo(String taskName, String dsName, String dbName, String tableName,
                                 String tableSchema)
            throws IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("taskName", taskName);
        params.put("dsName", dsName);
        params.put("dbName", dbName);
        params.put("tableName", tableName);
        params.put("tableSchema", tableSchema);
        String response = HttpUtil.get(String.format("http://%s:%s/naga/v1/param/output/bloodline", server, port), params);
        return Boolean.parseBoolean(response);
    }

}
