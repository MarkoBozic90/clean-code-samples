package tech.bozicmarko.directoryservice.service.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.bozicmarko.directoryservice.api.dto.response.PlayerResponse;
import tech.bozicmarko.directoryservice.api.dto.response.PlayerSummaryResponse;
import tech.bozicmarko.directoryservice.api.dto.response.UserSummaryResponse;
import tech.bozicmarko.directoryservice.service.PlayerDirectoryQuery;
import tech.bozicmarko.directoryservice.service.PlayerService;
import tech.bozicmarko.directoryservice.service.UserService;
@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class PlayerDirectoryQueryImpl implements PlayerDirectoryQuery {

    private static final int BATCH_MAX = 200;

    private final PlayerService playerService;
    private final UserService userService;

    @Cacheable(
        cacheNames = "playersByClub",
        key = "'v1:club:' + #clubId + ':players:summary'",
        unless = "#result == null || #result.isEmpty()"
    )
    @Override
    public List<PlayerSummaryResponse> listPlayerSummary(UUID clubId) {
        var players = playerService.searchByClubId(clubId);
        if (players.isEmpty()) return List.of();

        var userIds = players.stream().map(PlayerResponse::userId)
            .filter(Objects::nonNull).distinct().toList();

        var usersById = fetchUsersByIds(userIds);
        return players.stream()
            .map(p -> mapToSummary(p, usersById.get(p.userId())))
            .toList();
    }


    private Map<UUID, UserSummaryResponse> fetchUsersByIds(final List<UUID> userIds) {
        if (userIds.isEmpty()) return Map.of();

        Map<UUID, UserSummaryResponse> out = new HashMap<>(userIds.size() * 2);
        for (int i = 0; i < userIds.size(); i += BATCH_MAX) {
            List<UUID>  chunk = userIds.subList(i, Math.min(i + BATCH_MAX, userIds.size()));
            List<UserSummaryResponse> part = userService.findAllByUserIds(chunk);
            out.putAll(part.stream().collect(Collectors.toMap(
                UserSummaryResponse::id, Function.identity(), (a, b) -> a
            )));
        }
        return out;
    }

    private PlayerSummaryResponse mapToSummary(PlayerResponse p, UserSummaryResponse u) {
        if (u == null) {
            log.warn("User {} not found for player {}", p.userId(), p.userId());
        }
        String firstName = u != null ? u.firstName() : null;
        String lastName  = u != null ? u.lastName()  : null;
        String username  = u != null ? u.username()  : null;
        String city      = u != null ? u.city()      : null;

        URI avatar = safeUri(u != null ? u.imgUrl() : null);

        return new PlayerSummaryResponse(
            p.userId(),
            firstName,
            lastName,
            username,
            city,
            avatar,
            p.jerseyNumber(),
            p.primaryPosition(),
            Objects.requireNonNullElse(p.secondaryPositions(), List.of()),
            p.teamCode()

        );
    }

    private static URI safeUri(String s) {
        if (s == null || s.isBlank()) return null;
        try { return URI.create(s); }
        catch (IllegalArgumentException e) {
            return null;
        }
    }
}
