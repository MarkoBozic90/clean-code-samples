package tech.bozicmarko.playerservice.facade;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.bozicmarko.playerservice.api.response.PlayerCreateRequest;
import tech.bozicmarko.playerservice.api.response.PlayerVersionResponse;
import tech.bozicmarko.playerservice.dto.PlayerSummeryResponse;
import tech.bozicmarko.playerservice.dto.PlayerResponseDTO;
import tech.bozicmarko.playerservice.dto.PlayerUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface PlayerFacade {

    PlayerResponseDTO getPlayerById(final UUID id);

    Page<PlayerResponseDTO> searchAllPlayers(final Pageable pageable);

    PlayerResponseDTO createPlayer(final PlayerCreateRequest playerCreateDTO);

    PlayerResponseDTO updatePlayer(final UUID id, final PlayerUpdateDTO playerUpdateDTO);

    void deletePlayer(final UUID id);

    PlayerResponseDTO existsPlayer(final UUID id);

    List<PlayerResponseDTO> listPlayerByClubId(final UUID clubId);

    List<PlayerSummeryResponse> searchAllPlayersByClubId(UUID clubId);

    PlayerVersionResponse chackVersion(UUID clubId);
}
