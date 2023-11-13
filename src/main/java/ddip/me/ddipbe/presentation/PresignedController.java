package ddip.me.ddipbe.presentation;

import ddip.me.ddipbe.application.PresignedService;
import ddip.me.ddipbe.application.model.PresignedUrl;
import ddip.me.ddipbe.application.model.UploadType;
import ddip.me.ddipbe.global.dto.ResponseEnvelope;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("presigned-url")
@RestController
@RequiredArgsConstructor
public class PresignedController {

    private final PresignedService presignedService;

    @GetMapping
    public ResponseEnvelope<List<PresignedUrl>> getPresignedUrl(@RequestParam List<String> fileNames, @RequestParam UploadType type) {
        List<PresignedUrl> presignedUrls = fileNames.stream()
                .map(fileName -> presignedService.getPresignedUrl(fileName, type))
                .toList();
        return new ResponseEnvelope<>(presignedUrls);
    }
}
