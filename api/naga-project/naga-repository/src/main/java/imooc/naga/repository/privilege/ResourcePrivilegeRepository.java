package imooc.naga.repository.privilege;

import imooc.naga.entity.privilege.ResourcePrivilege;
import imooc.naga.entity.privilege.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourcePrivilegeRepository extends JpaRepository<ResourcePrivilege, Long> {
  ResourcePrivilege findOneByTeamAndResourceType(String team, ResourceType resourceType);

}
