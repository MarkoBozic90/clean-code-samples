package tech.bozicmarko.playerservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@NoArgsConstructor(force = true)
@AllArgsConstructor
@Entity
@Table(name = "Player", schema = "player_service_db")

public class Player extends Auditable {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false, updatable = false)
    private final UUID id;

    @Column(name = "user_id", nullable = false, unique = true, updatable = false)
    private final UUID userId;

    @Column(name ="club_id", nullable = false, updatable = false)
    private final UUID clubId;

    @Column(name = "jersey_number")
    private final Integer jerseyNumber;

    @Column(name = "position", length = 50)
    private final String position;

    @Column(name = "secondary_positions", length = 50)
    private final List<String>  secondaryPositions;

    @Column(name = "contract_image_url")
    private final String contractImageUrl;

    @Column(name = "team_code")
    private final String teamCode;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Statistic> statistics;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PhysicalAttributes> physicalAttributes;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PhysicalCapability> physicalCapabilities;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<PlayerClubHistory> clubHistory;
}
