package ru.practicum.statService.util;

import ru.practicum.statDto.dto.ViewStatsDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;

public class Mapper {
    public static final RowMapper<ViewStatsDto> STATS_DTO_ROW_MAPPER = (ResultSet rs, int rowNum) ->
            ViewStatsDto.builder()
            .uri(rs.getString("uri"))
            .app(rs.getString("app"))
            .hits(rs.getInt("hits_count"))
            .build();
}
