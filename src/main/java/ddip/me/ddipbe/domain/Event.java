package ddip.me.ddipbe.domain;

import ddip.me.ddipbe.global.util.JsonConverter;
import ddip.me.ddipbe.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Event extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    private String title;

    private Integer limitCount;

    private Integer remainCount;

    private String successContent;

    private String successImageUrl;

    private String thumbnailImageUrl;

    private ZonedDateTime startDateTime;

    private ZonedDateTime endDateTime;


    @Column(columnDefinition = "json")
    @Convert(converter = JsonConverter.class)
    private Map<String, Object> successForm;

    @OneToMany(mappedBy = "event", cascade = CascadeType.PERSIST)
    private List<SuccessRecord> successRecords = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Event(UUID uuid,
                 String title,
                 Integer limitCount,
                 String successContent,
                 String successImageUrl,
                 String thumbnailImageUrl,
                 ZonedDateTime startDateTime,
                 ZonedDateTime endDateTime,
                 Map<String, Object> successForm,
                 Member member) {
        this.uuid = uuid;
        this.title = title;
        this.limitCount = limitCount;
        this.remainCount = limitCount;
        this.successContent = successContent;
        this.successImageUrl = successImageUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.successForm = successForm;
        this.member = member;
    }

    public boolean isOpen(ZonedDateTime now) {
        return startDateTime.isBefore(now) && endDateTime.isAfter(now);
    }

    public boolean decreaseRemainCount() {
        if (remainCount == 0) {
            return false;
        }
        remainCount--;
        return true;
    }

    public void addSuccessRecord(SuccessRecord successRecord) {
        successRecords.add(successRecord);
    }

    public boolean hasSuccessRecord() {
        return !successRecords.isEmpty();
    }

    public boolean started() {
        return startDateTime.isBefore(ZonedDateTime.now());
    }

    public void update(
            String title,
            Integer limitCount,
            String successContent,
            String successImageUrl,
            ZonedDateTime startDateTime,
            ZonedDateTime endDateTime,
            Map<String, Object> successForm
    ) {
        this.title = title;
        this.limitCount = limitCount;
        this.remainCount = limitCount;
        this.successContent = successContent;
        this.successImageUrl = successImageUrl;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.successForm = successForm;
    }
}