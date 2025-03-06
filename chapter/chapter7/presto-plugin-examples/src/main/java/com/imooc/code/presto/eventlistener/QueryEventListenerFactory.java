package com.imooc.code.presto.eventlistener;

import com.facebook.presto.spi.eventlistener.EventListener;
import com.facebook.presto.spi.eventlistener.EventListenerFactory;

import java.util.Map;

public class QueryEventListenerFactory implements EventListenerFactory {
    @Override
    public String getName() {
        return "query-event-listener";
    }

    @Override
    public EventListener create(Map<String, String> config) {
        if(!config.containsKey("log.path")){
            throw new RuntimeException("missing lof.path conf");
        }
        return new QueryEventListener(config);
    }
}
