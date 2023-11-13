package ddip.me.ddipbe.domain;

import ddip.me.ddipbe.global.util.JsonConverter;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SuccessRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonConverter.class)
    @Nullable
    private Map<String, String> jsonString;

    public SuccessRecord(String token, Event event) {
        this.token = token;
        this.event = event;
    }

    public void createJsonString(Map<String, String> jsonString){
        this.jsonString = jsonString;
    }
}