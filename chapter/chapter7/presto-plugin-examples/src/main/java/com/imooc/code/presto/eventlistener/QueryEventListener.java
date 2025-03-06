package com.imooc.code.presto.eventlistener;

import com.facebook.presto.spi.eventlistener.EventListener;
import com.facebook.presto.spi.eventlistener.QueryCompletedEvent;
import com.facebook.presto.spi.eventlistener.QueryCreatedEvent;
import com.facebook.presto.spi.eventlistener.SplitCompletedEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;

public class QueryEventListener implements EventListener {
    private Map<String, String> config;
    private String logPath;

    public QueryEventListener(Map<String, String> config) {
        this.config = config;
        logPath = config.get("log.path");
        System.out.println(logPath);
    }

    @Override
    public void queryCreated(QueryCreatedEvent queryCreatedEvent) {
        String queryId = queryCreatedEvent.getMetadata().getQueryId();
        String query = queryCreatedEvent.getMetadata().getQuery();
        String user = queryCreatedEvent.getContext().getUser();
        String fileName = logPath + File.separator + queryId;
        File logFile = new File(fileName);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.append(String.format("User:%s Id:%s Query:%s\n", user, queryId, query));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void queryCompleted(QueryCompletedEvent queryCompletedEvent) {
        String queryId = queryCompletedEvent.getMetadata().getQueryId();
        long createTime = queryCompletedEvent.getCreateTime().toEpochMilli();
        long endTime = queryCompletedEvent.getEndTime().toEpochMilli();
        long totalBytes = queryCompletedEvent.getStatistics().getTotalBytes();
        String queryState = queryCompletedEvent.getMetadata().getQueryState();

        queryCompletedEvent.getFailureInfo().ifPresent(queryFailureInfo -> {
            int code = queryFailureInfo.getErrorCode().getCode();
            String s = queryFailureInfo.getFailureType().orElse("").toUpperCase();
            String s1 = queryFailureInfo.getFailureHost().orElse("");
            String s2 = queryFailureInfo.getFailureMessage().orElse("");
        });

        String fileName = logPath + File.separator + queryId;
        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.append(String.format("Id:%s StartTime:%s EndTime:%s State:%s\n", queryId, createTime, endTime, queryState));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void splitCompleted(SplitCompletedEvent splitCompletedEvent) {
        long createTime = splitCompletedEvent.getCreateTime().toEpochMilli();
        long endTime = splitCompletedEvent.getEndTime().orElse(Instant.MAX).toEpochMilli();
        String queryId = splitCompletedEvent.getQueryId();
        String stageId = splitCompletedEvent.getStageId();
        String taskId = splitCompletedEvent.getTaskId();

        String fileName = logPath + File.separator + queryId;
        try (FileWriter fw = new FileWriter(fileName, true)) {
            fw.append(String.format("Id:%s StartTime:%s EndTime:%s StageId:%s TaskId:%s\n", queryId, createTime, endTime, stageId, taskId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
