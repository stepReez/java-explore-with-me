package ru.practicum.service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface StatService {

    void postHit(String service, HttpServletRequest request);

    long getViews(List<String> uris);
}
