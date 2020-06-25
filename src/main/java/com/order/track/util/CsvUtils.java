package com.order.track.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class CsvUtils {

	public static <T> List<T> read(final Class<T> clazz, final InputStream stream) throws IOException {
		final CsvMapper mapper = new CsvMapper();
		final CsvSchema schema = mapper.schemaFor(clazz).withHeader().withColumnReordering(true);
		final ObjectReader reader = mapper.readerFor(clazz).with(schema);
		return reader.<T>readValues(stream).readAll();
	}
}
