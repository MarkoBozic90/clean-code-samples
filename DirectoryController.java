package tech.bozicmarko.directoryservice.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.bozicmarko.directoryservice.api.dto.response.PlayerSummaryResponse;
import tech.bozicmarko.directoryservice.facade.DirectoryFacade;

@Tag(name = "Directory service")
@RestController
@RequestMapping("/api/v1/directory")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DirectoryController {

    private final DirectoryFacade facade;

    @Operation(summary = "Get players from club (ETag/304 + Redis cache)")
    @ApiResponse(responseCode = "200", description = "OK")
    @ApiResponse(responseCode = "304", description = "Not Modified")
    @GetMapping("/{clubId}/players")
    public ResponseEntity<List<PlayerSummaryResponse>> listPlayers(
        @PathVariable UUID clubId,
        @RequestHeader(name = "If-None-Match", required = false) String ifNoneMatch) {

        var res = facade.listPlayerSummaryWithEtag(clubId, ifNoneMatch);

        if (res.notModified()) {
            return ResponseEntity.status(304)
                .eTag(res.etag())
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePrivate())
                .body(null);
        }

        return ResponseEntity.ok()
            .eTag(res.etag())
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS).cachePrivate())
            .body(res.body());
    }
}