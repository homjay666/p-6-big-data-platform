package com.imooc.code.presto.eventlistener;

import com.facebook.presto.spi.Plugin;
import com.facebook.presto.spi.eventlistener.EventListenerFactory;

import java.util.Arrays;

public class QueryEventPlugin implements Plugin {

    @Override
    public Iterable<EventListenerFactory> getEventListenerFactories(){
        QueryEventListenerFactory queryEventListenerFactory = new QueryEventListenerFactory();
        return Arrays.asList(queryEventListenerFactory);
    }
}
