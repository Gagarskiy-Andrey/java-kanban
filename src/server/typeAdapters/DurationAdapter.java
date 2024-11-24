package server.typeAdapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

public class DurationAdapter extends TypeAdapter<Duration> {
    @Override
    public void write(JsonWriter jsonWriter, Duration duration) throws IOException {
        if (Objects.isNull(duration)) {
            jsonWriter.value("null");
        } else {
            jsonWriter.value(duration.toString());
        }
    }

    @Override
    public Duration read(JsonReader jsonReader) throws IOException {
        // Проверяем на наличие NULL
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        } else {
            String durationString = jsonReader.nextString();

            // Обработка строки 'null'
            if ("null".equals(durationString)) {
                return null;
            }

            // Пытаемся распарсить строку в объект Duration
            return Duration.parse(durationString);
        }
    }
}
