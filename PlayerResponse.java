package tech.bozicmarko.directoryservice.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Schema(description = "Player response data")
public record PlayerResponse(

    @Schema(description = "user ID", format = "uuid", example = "22222222-2222-2222-2222-222222222222") UUID userId,

    @Schema(description = "Jersey number for player") Integer jerseyNumber,

    @Schema(description = "Primary position for player") @NotNull String primaryPosition,

    @Schema(description = "Secondary positions for player") @NotNull List<String> secondaryPositions,

    @Schema(description = "Code for team ", example = "U19 || SEN") @NotNull String teamCode) {


}
