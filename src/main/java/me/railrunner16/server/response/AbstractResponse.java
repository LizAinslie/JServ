package me.railrunner16.server.response;

import me.railrunner16.server.request.Request;

public abstract class AbstractResponse{
    public abstract Response getResponse(Request req);
}
