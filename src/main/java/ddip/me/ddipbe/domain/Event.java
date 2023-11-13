package ddip.me.ddipbe.domain;

import ddip.me.ddipbe.global.util.JsonConverter;
import jakarta.annotation.Nullable;
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

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    private String title;

    private Integer limitCount;

    private Integer remainCount;

    private String successContent;

    private ZonedDateTime startDateTime;

    private ZonedDateTime endDateTime;

    @OneToMany(mappedBy = "event", cascade = CascadeType.PERSIST)
    private List<SuccessRecord> successRecords = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "json")
    @Convert(converter = JsonConverter.class)
    @Nullable
    private Map<String, String> jsonString;

    private String imgUrl;

    private String successImgUrl;

    public Event(UUID uuid, String title, Integer limitCount, String successContent, ZonedDateTime startDateTime, ZonedDateTime endDateTime, Member member, Map<String,String> jsonString, String imgUrl, String successImgUrl) {
        this.uuid = uuid;
        this.title = title;
        this.limitCount = limitCount;
        this.remainCount = limitCount;
        this.successContent = successContent;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.member = member;
        this.jsonString = jsonString;
        this.imgUrl = imgUrl;
        this.successImgUrl = successImgUrl;
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

    public void addPermit(SuccessRecord successRecord) {
        successRecords.add(successRecord);
    }
}