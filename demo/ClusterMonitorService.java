import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClusterMonitorService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, Object> getYarnStatus() {
        // 这里应该调用Yarn REST API
        // 示例URL: http://resourcemanager:8088/ws/v1/cluster/metrics
        try {
            return restTemplate.getForObject("http://resourcemanager:8088/ws/v1/cluster/metrics", Map.class);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to get YARN status: " + e.getMessage());
            return error;
        }
    }

    public Map<String, Object> getClusterNodes() {
        // 这里应该调用Yarn REST API获取节点信息
        // 示例URL: http://resourcemanager:8088/ws/v1/cluster/nodes
        try {
            return restTemplate.getForObject("http://resourcemanager:8088/ws/v1/cluster/nodes", Map.class);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to get cluster nodes: " + e.getMessage());
            return error;
        }
    }
}