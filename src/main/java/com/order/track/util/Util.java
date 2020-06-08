package com.order.track.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

public final class Util {

    public static ByteArrayOutputStream loadInputStreamAsByteArrayOutputStream(final InputStream pInputStream)
	    throws IOException {

	final BufferedInputStream bufferedInputStream = new BufferedInputStream(pInputStream);
	final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	int result = bufferedInputStream.read();
	while (result != -1) {
	    byteArrayOutputStream.write((byte) result);
	    result = bufferedInputStream.read();
	}
	return byteArrayOutputStream;
    }

    /**
     * Converts string with separator to list . For example for input string "A,B,C"
     * output will be [A,B,C]
     */
    public static final BiFunction<String, String, List<String>> convertStringWithSeparatorToList = (input,
	    separator) -> {

	return Stream.of(input.split(separator)).collect(Collectors.toList());

    };

    /**
     * Converts string with separator to Set . For example for input string "A,B,C"
     * output will be [A,B,C]
     */
    public static final BiFunction<String, String, Set<String>> convertStringWithSeparatorToSet = (input,
	    separator) -> {

	return Sets.newHashSet(convertStringWithSeparatorToList.apply(input, separator));

    };

    /**
     * Converts string with separator to list . For example for input string "A,B,C"
     * output will be [A,B,C]
     */
    public static final BiFunction<String, String, List<String>> convertStringWithSeparatorToMap = (input,
	    separator) -> {

	return Stream.of(input.split(separator)).collect(Collectors.toList());

    };

    public static Class<?> determineGenericsType(Type type) {
	Class<?> result = null;
	if (type != null && ParameterizedType.class.isAssignableFrom(type.getClass())) {
	    final Type genericType = ((ParameterizedType) type).getActualTypeArguments()[0];
	    if (genericType != null) {
		if (genericType instanceof Class) {
		    result = (Class<?>) genericType;
		} else if (genericType instanceof ParameterizedType) {
		    final Type rawType = ((ParameterizedType) genericType).getRawType();
		    result = (Class<?>) rawType;
		}
	    }
	}
	return result;
    }

}
