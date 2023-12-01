package ddip.me.ddipbe.global.entity;

import ddip.me.ddipbe.global.util.CustomClock;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@MappedSuperclass
public abstract class BaseTimeEntity {

    @Column(updatable = false)
    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    public BaseTimeEntity() {
        createdAt = updatedAt = CustomClock.now();
    }

    @PrePersist
    public void prePersist() {
        createdAt = updatedAt = CustomClock.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = CustomClock.now();
    }
}
