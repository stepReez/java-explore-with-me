package ru.practicum.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.practicum.service.StatService;
import ru.practicum.statClient.client.Client;
import ru.practicum.statDto.dto.ViewStatsDto;
import ru.practicum.util.Constants;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StatServiceImpl implements StatService {

    private final Client client = new Client(new RestTemplate());

    @Override
    public void postHit(String service, HttpServletRequest request) {
        client.postHit("GetEvents", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        log.info("Hit from app {} done", service);
    }

    @Override
    public long getViews(ArrayList<String> uris) {
        List<ViewStatsDto> viewStats = client.getStats(LocalDateTime.now().minusYears(Constants.PLUS_MINUS_100_YEARS),
                LocalDateTime.now().plusYears(Constants.PLUS_MINUS_100_YEARS), uris, true);
        log.info(viewStats.toString());
        return viewStats.size();
    }
}
