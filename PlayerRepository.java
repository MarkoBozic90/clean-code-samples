package tech.bozicmarko.playerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tech.bozicmarko.playerservice.api.response.PlayerVersionResponse;
import tech.bozicmarko.playerservice.model.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {

    Optional<List<Player>> findAllByClubId(UUID clubId);

    @Query("""
           select new tech.bozicmarko.playerservice.api.response.PlayerVersionResponse(
             count(p),
             coalesce(max(p.updatedAt), max(p.createdAt))
           )
           from Player p
           where p.clubId = :clubId
        """)    PlayerVersionResponse versionForClub(@Param("clubId") UUID clubId);
}
