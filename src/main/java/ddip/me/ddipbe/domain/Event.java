package ddip.me.ddipbe.domain;

import ddip.me.ddipbe.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
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

    private String thumbnailImageUrl;

    private Applicants applicants;

    private SuccessResult successResult;

    private EventDuration eventDuration;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id")
    private Member member;

    public Event(String title,
                 Integer limitCount,
                 String successContent,
                 String successImageUrl,
                 String thumbnailImageUrl,
                 ZonedDateTime startDateTime,
                 ZonedDateTime endDateTime,
                 Map<String, Object> successForm,
                 Member member) {
        this.uuid = UUID.randomUUID();
        this.title = title;
        this.applicants = new Applicants(limitCount);
        this.successResult = new SuccessResult(successContent, successImageUrl, successForm);
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.eventDuration = new EventDuration(startDateTime, endDateTime);
        this.member = member;
    }

    public void update(
            String title,
            Integer limitCount,
            String successContent,
            String successImageUrl,
            String thumbnailImageUrl,
            ZonedDateTime startDateTime,
            ZonedDateTime endDateTime,
            Map<String, Object> successForm) {
        this.title = title;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.applicants = new Applicants(limitCount);
        this.successResult = new SuccessResult(successContent, successImageUrl, successForm);
        this.eventDuration = new EventDuration(startDateTime, endDateTime);
    }

    public void apply(SuccessRecord successRecord) {
        applicants.addSuccessRecord(successRecord);
    }

    public boolean isOwnedBy(long memberId) {
        return this.member.getId().equals(memberId);
    }

    public boolean isEditable() {
        return !applicants.exists() && !eventDuration.started();
    }

    public boolean isDeletable() {
        return !applicants.exists();
    }
}