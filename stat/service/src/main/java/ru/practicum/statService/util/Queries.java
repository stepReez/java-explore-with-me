package ru.practicum.statService.util;

public class Queries {
    public static final String SELECT_UNIQUE_STATS_WITH_URIS = "SELECT app, uri, count(DISTINCT ip) AS hits_count " +
            "FROM hits " +
            "WHERE time_stamp BETWEEN :start AND :end " +
            "AND uri IN (:uris) " +
            "GROUP BY app, uri " +
            "ORDER BY hits_count DESC";

    public static final String SELECT_NON_UNIQUE_STATS_WITH_URIS = "SELECT app, uri, count(id) AS hits_count " +
            "FROM hits " +
            "WHERE time_stamp BETWEEN :start and :end " +
            "AND uri IN (:uris) " +
            "GROUP BY app, uri " +
            "ORDER BY hits_count DESC";

    public static final String SELECT_UNIQUE_STATS_WITHOUT_URIS = "SELECT app, uri, count(DISTINCT ip) AS hits_count " +
            "FROM hits " +
            "WHERE time_stamp BETWEEN :start AND :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits_count DESC";

    public static final String SELECT_NON_UNIQUE_STATS_WITHOUT_URIS = "SELECT app, uri, count(id) AS hits_count " +
            "FROM hits " +
            "WHERE time_stamp BETWEEN :start and :end " +
            "GROUP BY app, uri " +
            "ORDER BY hits_count DESC";
}
