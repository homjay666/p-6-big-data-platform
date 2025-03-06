package naga.plugin.example;

import imooc.naga.plugin.sdk.PluginConfig;
import imooc.naga.plugin.sdk.PluginParam;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DemoConfig implements PluginConfig {

    @PluginParam(name = "key")
    private String key;

    @PluginParam(name = "param")
    private String param;

    @PluginParam(name = "array")
    private String[] array;

    @PluginParam(name = "list")
    private List<Integer> list;

    @PluginParam(name = "map")
    private Map<String, String> map;

}
