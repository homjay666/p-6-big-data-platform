package imooc.naga.plugin.sdk;

public interface NagaPlugin {
    public PluginContext getContext();

    public void execute() throws Exception;
}
