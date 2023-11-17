package ddip.me.ddipbe.domain;

import ddip.me.ddipbe.domain.exception.EventCapacityFullException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Applicants {

    private Integer limitCount;

    private Integer remainCount;

    @OneToMany(mappedBy = "event", cascade = CascadeType.PERSIST)
    private List<SuccessRecord> successRecords = new ArrayList<>();

    public Applicants(Integer limitCount) {
        this.limitCount = limitCount;
        this.remainCount = limitCount;
    }

    public void addSuccessRecord(SuccessRecord successRecord) {
        if (remainCount == 0) {
            throw new EventCapacityFullException();
        }
        remainCount--;
        successRecords.add(successRecord);
    }

    public boolean exists() {
        return !successRecords.isEmpty();
    }
}
