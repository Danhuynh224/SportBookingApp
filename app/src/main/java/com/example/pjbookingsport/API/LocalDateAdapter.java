package com.example.pjbookingsport.API;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends TypeAdapter<LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public void write(JsonWriter out, LocalDate value) throws IOException {
        out.value(value != null ? value.format(formatter) : null);
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        String dateString = in.nextString();

        if (dateString == null || dateString.isEmpty() || dateString.equals("0000-00-00")) {
            return null;
        }

        return LocalDate.parse(dateString, formatter);
    }

}

