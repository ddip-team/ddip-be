package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.DummyDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Profile("dev")
@RequestMapping("test")
@RestController
@RequiredArgsConstructor
public class TestController {

    private final DummyDataService dummyDataService;

    @PostMapping("init-data")
    public UUID initData() {
        return dummyDataService.initDataAndReturnOngoingEventUuid();
    }

    @PostMapping("reset-event/{uuid}")
    public void resetEvent(@PathVariable UUID uuid) {
        dummyDataService.resetEvent(uuid);
    }
}
