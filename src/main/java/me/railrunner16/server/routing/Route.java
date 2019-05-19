package me.railrunner16.server.routing;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.railrunner16.server.request.RequestMethod;

@AllArgsConstructor
@Getter
public class Route {
    private String uri;
    private RequestMethod method;
}
