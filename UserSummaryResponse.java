package tech.bozicmarko.directoryservice.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Schema(description = "Lightweight summary of a user")
public record UserSummaryResponse(
    @Schema(description = "User id ",format = "uuid", example = "22222222-2222-2222-2222-222222222222")
    @NotNull
    UUID id,

    @Schema(description = "Username ", example = "zemun")
    @NotBlank
    String username,

    @Schema(description = "First name", example = "Marko")
    @NotBlank @Size(max = 64)
    String firstName,

    @Schema(description = "Last name", example = "Bozic")
    @NotBlank @Size(max = 64)
    String lastName,
    @Schema(description = "City", example = "Belgrade")
    @NotBlank @Size(max = 96)
    String city,
    @Schema(description = "Avatar image URL", format = "uri", example = "https://cdn.example.com/avatars/john.png", nullable = true)
    String imgUrl
) {}