package ddip.me.ddipbe.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
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

    private Integer permitCount;

    private String content;

    private LocalDateTime start;

    private LocalDateTime end;

    @OneToMany(mappedBy = "event", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Permit> permits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Event(UUID uuid, String title, Integer permitCount, String content, LocalDateTime start, LocalDateTime end, Member member) {
        this.uuid = uuid;
        this.title = title;
        this.permitCount = permitCount;
        this.content = content;
        this.start = start;
        this.end = end;
        this.member = member;
    }
}
