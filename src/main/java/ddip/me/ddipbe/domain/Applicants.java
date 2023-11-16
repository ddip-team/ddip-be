package ddip.me.ddipbe.domain;

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
}
