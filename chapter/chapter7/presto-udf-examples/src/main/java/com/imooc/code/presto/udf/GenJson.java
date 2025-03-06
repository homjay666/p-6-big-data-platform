package com.imooc.code.presto.udf;

import com.facebook.presto.spi.function.Description;
import com.facebook.presto.spi.function.ScalarFunction;
import com.facebook.presto.spi.function.SqlNullable;
import com.facebook.presto.spi.function.SqlType;
import com.facebook.presto.spi.type.StandardTypes;
import io.airlift.slice.Slice;
import io.airlift.slice.Slices;

public class GenJson {
    @ScalarFunction("GenJson")
    @Description("gen json str")
    @SqlType(StandardTypes.VARCHAR)
    public static Slice genJson(@SqlType(StandardTypes.VARCHAR) Slice key, @SqlType(StandardTypes.VARCHAR) @SqlNullable Slice value) {
        return Slices.utf8Slice(String.format("{\"%s\":\"%s\"}", key.toStringUtf8(), value == null ? "" : value.toStringUtf8()));
    }
}
