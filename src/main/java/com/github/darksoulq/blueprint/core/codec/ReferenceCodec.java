package com.github.darksoulq.blueprint.core.codec;

import com.github.darksoulq.abyssallib.common.serialization.Codec;
import com.github.darksoulq.abyssallib.common.serialization.Codecs;
import com.github.darksoulq.abyssallib.common.serialization.DataResult;

import java.util.function.Function;

public class ReferenceCodec {

    public static <T> Codec<T> referenceable(Codec<T> inlineCodec, Function<String, T> lazyProxyCreator) {
        return Codec.fallback(
            Codecs.STRING.flatXmap(
                str -> {
                    if (str.startsWith("#")) {
                        String key = str.substring(1);
                        T resolved = lazyProxyCreator.apply(key);
                        if (resolved != null) {
                            return DataResult.success(resolved);
                        }
                        return DataResult.error("Reference not found in registry: " + key);
                    }
                    return DataResult.error("String is not a valid reference indicator.");
                },
                obj -> DataResult.error("Encoding references dynamically is currently unsupported")
            ),
            inlineCodec
        );
    }
}