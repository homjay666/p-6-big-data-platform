import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class SparkService {

    @Value("${spark.master-url}")
    private String masterUrl;
    
    @Value("${spark.deploy-mode}")
    private String deployMode;
    
    @Value("${spark.executor-memory}")
    private String executorMemory;
    
    @Value("${spark.executor-cores}")
    private String executorCores;

    public Map<String, Object> submitSparkJob(String jarPath, String mainClass, List<String> args) {
        Map<String, Object> result = new HashMap<>();
        
        List<String> command = new ArrayList<>();
        command.add("spark-submit");
        command.add("--master");
        command.add(masterUrl);
        command.add("--deploy-mode");
        command.add(deployMode);
        command.add("--executor-memory");
        command.add(executorMemory);
        command.add("--executor-cores");
        command.add(executorCores);
        command.add("--class");
        command.add(mainClass);
        command.add(jarPath);
        
        if (args != null && !args.isEmpty()) {
            command.addAll(args);
        }
        
        CompletableFuture.runAsync(() -> {
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                
                int exitCode = process.waitFor();
                System.out.println("Spark job completed with exit code: " + exitCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        result.put("status", "submitted");
        result.put("command", String.join(" ", command));
        return result;
    }

    public Map<String, Object> getSparkApplicationStatus() {
        // 这里需要调用Spark REST API获取应用程序状态
        // 由于实现较为复杂，这里仅提供示例框架
        Map<String, Object> status = new HashMap<>();
        status.put("activeApplications", 5);
        status.put("completedApplications", 10);
        return status;
    }
}
