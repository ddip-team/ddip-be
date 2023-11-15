package ddip.me.ddipbe.domain;

import ddip.me.ddipbe.global.util.JsonConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Map;

@Getter
@Entity
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
    private Map<String, Object> formInputValue;

    private ZonedDateTime timestamp;

    public SuccessRecord(String token, Event event, ZonedDateTime timestamp) {
        this.token = token;
        this.event = event;
        this.timestamp = timestamp;
    }

    public boolean registerFormInputValue(Map<String, Object> formInputValue) {
        if (this.formInputValue == null) {
            this.formInputValue = formInputValue;
            return true;
        }
        return false;
    }
}