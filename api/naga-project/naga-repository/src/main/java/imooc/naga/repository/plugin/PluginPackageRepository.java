package imooc.naga.repository.plugin;

import imooc.naga.entity.plugin.PluginPackage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PluginPackageRepository extends JpaRepository<PluginPackage, Long> {
  PluginPackage findByNameAndVersion(String name, String version);

  List<PluginPackage> findByName(String name);
}
