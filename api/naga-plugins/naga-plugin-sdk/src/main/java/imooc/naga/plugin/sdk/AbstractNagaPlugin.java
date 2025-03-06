package imooc.naga.plugin.sdk;

public abstract class AbstractNagaPlugin<T extends PluginConfig> implements NagaPlugin {

  private PluginContext pluginContext;

  public AbstractNagaPlugin() {
    this.init();
  }

  protected void init() {
    try {
      pluginContext = PluginContextFactory.getOrCreateContext(this.getClass());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public PluginContext getContext() {
    return this.pluginContext;
  }

  public T getConfig() {
    return (T) this.pluginContext.getConfig();
  }


}
