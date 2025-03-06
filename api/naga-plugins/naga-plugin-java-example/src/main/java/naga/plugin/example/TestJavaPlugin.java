package naga.plugin.example;

import imooc.naga.plugin.sdk.AbstractNagaPlugin;
import imooc.naga.plugin.sdk.JsonUtil;
import imooc.naga.plugin.sdk.PluginOutputValues;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.util.*;

public class TestJavaPlugin extends AbstractNagaPlugin<DemoConfig> {
    public static void main(String[] args) throws Exception {
        TestJavaPlugin plugin = new TestJavaPlugin();
        plugin.execute();
    }

    @Override
    public void execute() throws Exception {
        System.out.println(this.getContext().getFlowExecId());
        System.out.println(this.getContext().getJobName());
        System.out.println(this.getContext().getFlowName());
        System.out.println(this.getContext().getTaskName());
        System.out.println(this.getContext().getRuntimeJobId());
        System.out.println(this.getContext().getFlowExecId());
        System.out.println(this.getConfig().getKey());
        System.out.println(this.getConfig().getParam());
        System.out.println(JsonUtil.toJson(this.getConfig()));

        PluginOutputValues outputValues = new PluginOutputValues();
        Map<String, Object> map = new HashMap<>();
        map.put("a", "a");
        map.put("b", "b");
        outputValues.setMap("map", map);
        List list = new ArrayList<>();
        list.add("1");
        list.add("5");
        outputValues.setList("list", list);
        outputValues.setValue("param", 12);
        this.getContext().outputValues(outputValues);
        FileSystem fs = this.getContext().getFileSystem();

        Arrays.stream(fs.listStatus(new Path("/"))).forEach(fileStatus -> {
            System.out.println(fileStatus.getPath());
        });

    }
}