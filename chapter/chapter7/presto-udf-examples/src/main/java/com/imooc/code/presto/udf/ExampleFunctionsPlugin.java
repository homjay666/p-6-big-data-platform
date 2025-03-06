package com.imooc.code.presto.udf;

import com.facebook.presto.spi.Plugin;
import com.google.common.collect.ImmutableSet;

import java.util.Set;

public class ExampleFunctionsPlugin implements Plugin {
    @Override
    public Set<Class<?>> getFunctions(){
        return ImmutableSet.<Class<?>>builder()
                .add(PrefixFunction.class)
                .add(GenJson.class)
                .add(ConCatFunction.class).build();
    }
}
