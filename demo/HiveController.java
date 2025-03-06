
import com.example.hadoopweb.service.HiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hive")
@CrossOrigin(origins = "*")
public class HiveController {

    @Autowired
    private HiveService hiveService;

    @PostMapping("/query")
    public ResponseEntity<Map<String, Object>> executeQuery(@RequestBody Map<String, String> requestBody) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String sql = requestBody.get("sql");
            List<Map<String, Object>> results = hiveService.executeQuery(sql);
            
            response.put("success", true);
            response.put("data", results);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "查询执行失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/tables")
    public ResponseEntity<List<String>> showTables() {
        try {
            return ResponseEntity.ok(hiveService.showTables());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/describe/{tableName}")
    public ResponseEntity<List<Map<String, Object>>> describeTable(@PathVariable String tableName) {
        try {
            return ResponseEntity.ok(hiveService.describeTable(tableName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}