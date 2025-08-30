package tech.bozicmarko.playerservice.mapper;

import org.mapstruct.*;
import tech.bozicmarko.playerservice.api.response.PlayerCreateRequest;
import tech.bozicmarko.playerservice.dto.PlayerResponseDTO;
import tech.bozicmarko.playerservice.dto.PlayerUpdateDTO;
import tech.bozicmarko.playerservice.mapper.support.TemporalMapper;
import tech.bozicmarko.playerservice.model.Player;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { StatisticMapper.class, PhysicalAttributesMapper.class, PhysicalCapabilityMapper.class, PlayerClubHistoryMapper.class, TemporalMapper.class }
)
public interface PlayerMapper {

    @Mapping(source = "id",                 target = "id")
    @Mapping(source = "userId",             target = "userId")
    @Mapping(source = "clubId",             target = "clubId")
    @Mapping(source = "jerseyNumber",       target = "jerseyNumber")
    @Mapping(source = "position",           target = "position")
    @Mapping(source = "contractImageUrl",   target = "contractImageUrl")
    @Mapping(source = "statistics",         target = "statistics")
    @Mapping(source = "physicalAttributes", target = "physicalAttributes")
    @Mapping(source = "physicalCapabilities", target = "physicalCapabilities")
    @Mapping(source = "clubHistory",        target = "clubHistory")
    @Mapping(source = "createdAt",          target = "createdAt")
    @Mapping(source = "createdBy",          target = "createdBy")
    @Mapping(source = "updatedAt",          target = "updatedAt")
    @Mapping(source = "updatedBy",          target = "updatedBy")
    PlayerResponseDTO toDto(Player entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId",            target = "userId")
    @Mapping(source = "clubId",            target = "clubId")
    @Mapping(source = "jerseyNumber",      target = "jerseyNumber")
    @Mapping(source = "primaryPosition",   target = "position")
    @Mapping(source = "secondaryPositions",target = "secondaryPositions")
    @Mapping(source = "teamCode",          target = "teamCode")
    @Mapping(target = "contractImageUrl",  ignore = true)
    @Mapping(target = "statistics",        ignore = true)
    @Mapping(target = "physicalAttributes",ignore = true)
    @Mapping(target = "physicalCapabilities", ignore = true)
    @Mapping(target = "clubHistory",       ignore = true)
    Player toEntity(PlayerCreateRequest dto);

    //todo update
    @BeanMapping(unmappedTargetPolicy = ReportingPolicy.ERROR)
    @Mapping(target = "id",                 expression = "java(existing.getId())")
    @Mapping(target = "userId",             expression = "java(existing.getUserId())")
    @Mapping(target = "clubId",             expression = "java(existing.getClubId())")
    @Mapping(target = "statistics",         expression = "java(existing.getStatistics())")
    @Mapping(target = "physicalAttributes", expression = "java(existing.getPhysicalAttributes())")
    @Mapping(target = "physicalCapabilities", expression = "java(existing.getPhysicalCapabilities())")
    @Mapping(target = "clubHistory",        expression = "java(existing.getClubHistory())")
    @Mapping(source = "jerseyNumber",       target = "jerseyNumber")
    @Mapping(source = "position",           target = "position")
    @Mapping(source = "teamCode",           target = "teamCode")
    @Mapping(source = "contractImageUrl",   target = "contractImageUrl")
    Player toUpdatedEntity(PlayerUpdateDTO dto, @Context Player existing);

    List<PlayerResponseDTO> toDtoList(List<Player> entities);

}
