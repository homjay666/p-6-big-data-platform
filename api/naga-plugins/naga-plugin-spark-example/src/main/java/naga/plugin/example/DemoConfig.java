package naga.plugin.example;

import imooc.naga.plugin.sdk.PluginConfig;
import imooc.naga.plugin.sdk.PluginParam;
import lombok.Data;

@Data
public class DemoConfig implements PluginConfig {

    @PluginParam(name = "input.file.path")
    private String inputFile;


    @PluginParam(name = "output.file.path")
    private String outputFilePath;
}
