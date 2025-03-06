import com.example.hadoopweb.service.SparkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/spark")
@CrossOrigin(origins = "*")
public class SparkController {

    @Autowired
    private SparkService sparkService;

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitJob(@RequestBody Map<String, Object> requestBody) {
        try {
            String jarPath = (String) requestBody.get("jarPath");
            String mainClass = (String) requestBody.get("mainClass");
            @SuppressWarnings("unchecked")
            List<String> args = (List<String>) requestBody.get("args");
            
            Map<String, Object> result = sparkService.submitSparkJob(jarPath, mainClass, args);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("message", "提交Spark作业失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getSparkStatus() {
        try {
            Map<String, Object> status = sparkService.getSparkApplicationStatus();
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}