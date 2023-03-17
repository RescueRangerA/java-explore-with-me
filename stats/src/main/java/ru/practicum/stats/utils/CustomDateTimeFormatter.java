package ru.practicum.stats.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

@Component
public class CustomDateTimeFormatter {

    private final DateTimeFormatter formatter;

    public CustomDateTimeFormatter(@Value("${ru.practicum.stats.jackson.date-format}") String pattern) {
        this.formatter = DateTimeFormatter.ofPattern(pattern);
    }

    public Optional<LocalDateTime> parse(CharSequence text) {
        try {
            return Optional.of(LocalDateTime.parse(text, this.formatter));
        } catch (DateTimeParseException e) {
            return Optional.empty();
        }
    }

    public String format(LocalDateTime localDateTime) {
        return localDateTime != null ? localDateTime.format(this.formatter) : null;
    }
}
