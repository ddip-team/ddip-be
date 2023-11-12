package ddip.me.ddipbe.global.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ParsingJson {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    public Map<String,String> parsingStringToMap(String parsingTarget){
        String parsingValue = parsingTarget.substring(1, parsingTarget.length() - 1).replace("\\\"", "\"");
        return objectMapper.readValue(parsingValue, new TypeReference<Map<String, String>>(){});
    }
}
