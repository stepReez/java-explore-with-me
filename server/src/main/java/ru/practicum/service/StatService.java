package ru.practicum.service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

public interface StatService {

    void postHit(String service, HttpServletRequest request);

    long getViews(ArrayList<String> uris);
}
