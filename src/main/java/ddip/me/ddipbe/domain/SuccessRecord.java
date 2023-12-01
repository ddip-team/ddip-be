package ddip.me.ddipbe.domain;

import ddip.me.ddipbe.domain.exception.SuccessFormAlreadyRegisteredException;
import ddip.me.ddipbe.global.entity.BaseTimeEntity;
import ddip.me.ddipbe.global.util.JsonConverter;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class SuccessRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> formInputValue;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "event_id")
    private Event event;

    public SuccessRecord(String token, Event event) {
        this.token = token;
        this.event = event;
    }

    public void registerFormInputValue(Map<String, Object> formInputValue) {
        if (this.formInputValue != null) {
            throw new SuccessFormAlreadyRegisteredException();
        }
        this.formInputValue = formInputValue;
    }
}