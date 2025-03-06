import com.example.hadoopweb.service.HDFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/hdfs")
@CrossOrigin(origins = "*")
public class HDFSController {

    @Autowired
    private HDFSService hdfsService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("path") String hdfsPath) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            boolean result = hdfsService.uploadFile(file, hdfsPath);
            response.put("success", result);
            response.put("message", "文件上传成功");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("success", false);
            response.put("message", "文件上传失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<Map<String, Object>>> listFiles(@RequestParam("path") String hdfsPath) {
        try {
            List<Map<String, Object>> files = hdfsService.listFiles(hdfsPath);
            return ResponseEntity.ok(files);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getHdfsStatus() {
        try {
            Map<String, Object> status = hdfsService.getHdfsStatus();
            return ResponseEntity.ok(status);
        } catch (IOException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
