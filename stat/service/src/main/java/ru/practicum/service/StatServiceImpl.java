package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.dto.HitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.Hit;
import ru.practicum.repository.HitRepository;
import ru.practicum.util.HitMapper;
import ru.practicum.util.Mapper;
import ru.practicum.util.Queries;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatServiceImpl implements StatService {

    private final HitRepository hitRepository;

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    @Override
    public HitDto hitRequest(HitDto hitDto) {
        Hit hit = hitRepository.save(HitMapper.toHit(hitDto));
        log.info("Hit with id = {} added", hit.getId());
        return HitMapper.toHitDto(hit);
    }

    @Override
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        LocalDateTime startDate = LocalDateTime.parse(
                URLDecoder.decode(start, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
        LocalDateTime endDate = LocalDateTime.parse(
                URLDecoder.decode(end, StandardCharsets.UTF_8),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("start", startDate);
        parameters.addValue("end", endDate);


        List<ViewStatsDto> viewStats;
        if (uris.isEmpty()) {
            if (unique) {
                viewStats = namedJdbcTemplate.query(
                        Queries.SELECT_UNIQUE_STATS_WITHOUT_URIS,
                        parameters,
                        Mapper.STATS_DTO_ROW_MAPPER);
                log.info("Unique stats between {} and {} found", startDate, endDate);
            } else {
                viewStats = namedJdbcTemplate.query(
                        Queries.SELECT_NON_UNIQUE_STATS_WITHOUT_URIS,
                        parameters,
                        Mapper.STATS_DTO_ROW_MAPPER);
                log.info("Stats between {} and {} found", startDate, endDate);
            }
        } else {
            parameters.addValue("uris", uris);
            if (unique) {
                viewStats = namedJdbcTemplate.query(
                        Queries.SELECT_UNIQUE_STATS_WITH_URIS,
                        parameters,
                        Mapper.STATS_DTO_ROW_MAPPER);
                log.info("Unique stats between {} and {} for uris {} found", startDate, endDate, uris);
            } else {
                viewStats = namedJdbcTemplate.query(
                        Queries.SELECT_NON_UNIQUE_STATS_WITH_URIS,
                        parameters,
                        Mapper.STATS_DTO_ROW_MAPPER);
                log.info("Stats between {} and {} for uris {} found", startDate, endDate, uris);
            }
        }
        return viewStats;
    }
}
