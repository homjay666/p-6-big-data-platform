package com.imooc.code.presto.udf;

import com.facebook.presto.spi.function.Description;
import com.facebook.presto.spi.function.ScalarFunction;
import com.facebook.presto.spi.function.SqlType;
import com.facebook.presto.spi.type.StandardTypes;
import io.airlift.slice.Slice;
import io.airlift.slice.Slices;

public class PrefixFunction {
    @ScalarFunction("Prefix")
    @Description("prefix string")
    @SqlType(StandardTypes.VARCHAR)
    public static Slice prefix(@SqlType(StandardTypes.VARCHAR) Slice value) {
        return Slices.utf8Slice("presto_udf_" + value.toStringUtf8());
    }
}
