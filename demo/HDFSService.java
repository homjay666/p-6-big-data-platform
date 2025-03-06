import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HDFSService {

    @Autowired
    private FileSystem fileSystem;

    public boolean uploadFile(MultipartFile file, String hdfsPath) throws IOException {
        try {
            Path destPath = new Path(hdfsPath + "/" + file.getOriginalFilename());
            fileSystem.copyFromLocalFile(false, true, 
                    new Path(file.getOriginalFilename()), destPath);
            return true;
        } catch (Exception e) {
            throw new IOException("上传文件到HDFS失败: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> listFiles(String hdfsPath) throws IOException {
        List<Map<String, Object>> fileList = new ArrayList<>();
        Path path = new Path(hdfsPath);
        
        org.apache.hadoop.fs.FileStatus[] statuses = fileSystem.listStatus(path);
        for (org.apache.hadoop.fs.FileStatus status : statuses) {
            Map<String, Object> fileInfo = new HashMap<>();
            fileInfo.put("fileName", status.getPath().getName());
            fileInfo.put("fileSize", status.getLen());
            fileInfo.put("isDirectory", status.isDirectory());
            fileInfo.put("modificationTime", status.getModificationTime());
            fileInfo.put("replication", status.getReplication());
            fileInfo.put("permission", status.getPermission().toString());
            fileList.add(fileInfo);
        }
        
        return fileList;
    }

    public Map<String, Object> getHdfsStatus() throws IOException {
        Map<String, Object> status = new HashMap<>();
        status.put("capacity", fileSystem.getStatus().getCapacity());
        status.put("used", fileSystem.getStatus().getUsed());
        status.put("remaining", fileSystem.getStatus().getRemaining());
        status.put("blockSize", fileSystem.getDefaultBlockSize());
        status.put("replication", fileSystem.getDefaultReplication());
        return status;
    }
}