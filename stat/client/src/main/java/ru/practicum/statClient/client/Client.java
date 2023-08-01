package ru.practicum.statClient.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.practicum.statDto.dto.HitDto;
import ru.practicum.statDto.dto.ViewStatsDto;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class Client {
    private final RestTemplate restTemplate;

    public HitDto postHit(String app, String uri, String ip, LocalDateTime timeStamp) {
        String resourceUrl = "http://localhost:9090/hit";
        HitDto hitDto = HitDto.builder()
                .app(app)
                .uri(uri)
                .ip(ip)
                .timestamp(timeStamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
        HttpEntity<HitDto> request = new HttpEntity<>(hitDto);
        HitDto response = restTemplate.postForObject(resourceUrl, request, HitDto.class);
        return response;
    }

    public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, ArrayList<String> uris, boolean unique) {
        String startDate = URLEncoder.encode(start.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                StandardCharsets.UTF_8);

        String endDate = URLEncoder.encode(end.format(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                StandardCharsets.UTF_8);

        StringBuilder resourceUrl = new StringBuilder("http://localhost:9090/stats?");
        resourceUrl.append(String.format("start=%s", startDate));
        resourceUrl.append(String.format("&end=%s", endDate));
        uris.forEach(u -> resourceUrl.append(String.format("&uris=%s",
                        URLEncoder.encode(u, StandardCharsets.UTF_8))));
        resourceUrl.append(String.format("&unique=%b", unique));

        ResponseEntity<List> response = restTemplate.getForEntity(resourceUrl.toString(), List.class);

        List<ViewStatsDto> viewStats = response.getBody();
        return viewStats;
    }
}
