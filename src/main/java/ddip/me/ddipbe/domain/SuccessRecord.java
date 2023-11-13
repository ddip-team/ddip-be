package ddip.me.ddipbe.domain;

import ddip.me.ddipbe.global.util.JsonConverter;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
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
    private Map<String, String> successInputInfo;

    private ZonedDateTime timestamp;

    public SuccessRecord(String token, Event event, ZonedDateTime timestamp) {
        this.token = token;
        this.event = event;
        this.timestamp = timestamp;
    }

    public void updateSuccessInputInfo(Map<String, String> successInputInfo){
        this.successInputInfo = successInputInfo;
    }
}