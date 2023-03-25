package ru.practicum.ewm.extension;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class WebUtils {

    private final HttpServletRequest request;

    public WebUtils(HttpServletRequest request) {
        this.request = request;
    }

    public String getClientIp() {
        return request.getRemoteAddr();
    }

    public String getRequestURI() {
        return request.getRequestURI();
    }
}