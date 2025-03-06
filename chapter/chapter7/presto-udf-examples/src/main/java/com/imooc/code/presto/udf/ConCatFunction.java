package com.imooc.code.presto.udf;

import com.facebook.presto.spi.block.BlockBuilder;
import com.facebook.presto.spi.function.*;
import com.facebook.presto.spi.type.StandardTypes;
import io.airlift.slice.Slice;
import io.airlift.slice.Slices;

import static com.facebook.presto.spi.type.VarcharType.VARCHAR;

@AggregationFunction("ConcatStr")
public class ConCatFunction {

    @InputFunction
    public static void input(StringValueState state, @SqlType(StandardTypes.VARCHAR) Slice value) {
        state.setStringValue(Slices.utf8Slice(checkNull(state.getStringValue()) + "|" + value.toStringUtf8()));
    }

    @CombineFunction
    public static void combine(StringValueState state, StringValueState otherState) {
        state.setStringValue(Slices.utf8Slice(checkNull(state.getStringValue()) + "|" + checkNull(otherState.getStringValue())));
    }

    @OutputFunction(StandardTypes.VARCHAR)
    public static void output(StringValueState state, BlockBuilder blockBuilder) {
        VARCHAR.writeSlice(blockBuilder, state.getStringValue());
    }

    private static String checkNull(Slice slice) {
        return slice == null ? "" : slice.toStringUtf8();
    }
}
