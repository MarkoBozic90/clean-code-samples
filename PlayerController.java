package tech.bozicmarko.playerservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.bozicmarko.playerservice.api.response.PlayerCreateRequest;
import tech.bozicmarko.playerservice.api.response.PlayerVersionResponse;
import tech.bozicmarko.playerservice.dto.PlayerSummeryResponse;
import tech.bozicmarko.playerservice.dto.PlayerResponseDTO;
import tech.bozicmarko.playerservice.dto.PlayerUpdateDTO;
import tech.bozicmarko.playerservice.facade.PlayerFacade;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Tag(name = "Players")
@RestController
@RequestMapping("/api/v1/players")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerFacade facade;

    @Operation(summary = "Get player by ID")
    @ApiResponse(responseCode = "200", description = "Found")
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> get(@PathVariable UUID id) {
        return ResponseEntity.ok(facade.getPlayerById(id));
    }

    @Operation(summary = "List players")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping
    public ResponseEntity<Page<PlayerResponseDTO>> list(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(facade.searchAllPlayers(pageable));
    }

    @Operation(summary = "List players (optionally filter by clubId)")
    @ApiResponse(responseCode = "200", description = "OK")
    @GetMapping("/by-club-id/{clubId}")
    public ResponseEntity<List<PlayerSummeryResponse>> list(
        @PathVariable(required = false) UUID clubId
    ) {
        return new ResponseEntity<>(facade.searchAllPlayersByClubId(clubId),HttpStatus.OK);
    }
    @GetMapping("/_version")
    ResponseEntity<PlayerVersionResponse> version(@RequestParam("clubId") UUID clubId){
        return new ResponseEntity<>(facade.chackVersion(clubId),HttpStatus.OK);
    }

    @Operation(summary = "Create player")
    @ApiResponse(responseCode = "201", description = "Created")
    @PostMapping
    public ResponseEntity<PlayerResponseDTO> create(@Valid @RequestBody PlayerCreateRequest body) {
        PlayerResponseDTO created = facade.createPlayer(body);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(created);
    }

    @Operation(summary = "Update player")
    @ApiResponse(responseCode = "200", description = "Updated")
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @PutMapping("/{id}")
    public ResponseEntity<PlayerResponseDTO> update(@PathVariable UUID id,
                                                    @Valid @RequestBody PlayerUpdateDTO body) {
        return ResponseEntity.ok(facade.updatePlayer(id, body));
    }

    @Operation(summary = "Delete player")
    @ApiResponse(responseCode = "204", description = "No Content")
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        facade.deletePlayer(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Check existence by ID")
    @ApiResponse(responseCode = "200", description = "Exists")
    @ApiResponse(responseCode = "404", description = "Not Found", content = @Content)
    @GetMapping("/{id}/exists")
    public ResponseEntity<PlayerResponseDTO> exists(@PathVariable UUID id) {
        return ResponseEntity.ok(facade.existsPlayer(id));
    }

}
