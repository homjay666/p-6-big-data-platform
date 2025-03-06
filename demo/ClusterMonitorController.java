package com.example.hadoopweb.controller;

import com.example.hadoopweb.service.ClusterMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/cluster")
@CrossOrigin(origins = "*")
public class ClusterMonitorController {

    @Autowired
    private ClusterMonitorService clusterMonitorService;

    @GetMapping("/yarn")
    public ResponseEntity<Map<String, Object>> getYarnStatus() {
        return ResponseEntity.ok(clusterMonitorService.getYarnStatus());
    }

    @GetMapping("/nodes")
    public ResponseEntity<Map<String, Object>> getClusterNodes() {
        return ResponseEntity.ok(clusterMonitorService.getClusterNodes());
    }
}

// 12. 全局异常处理
package com.example.hadoopweb.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> error = new HashMap<>();
        error.put("success", false);
        error.put("message", e.getMessage());
        
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
