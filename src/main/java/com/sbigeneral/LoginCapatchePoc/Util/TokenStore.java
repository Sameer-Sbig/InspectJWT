package com.sbigeneral.LoginCapatchePoc.Util;

import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class TokenStore {

    private final ConcurrentHashMap<String, String> tokenMap = new ConcurrentHashMap<>();

    public String getTokenForUser(String username) {
        return tokenMap.get(username);
    }

    public void storeToken(String username, String token) {
        tokenMap.put(username, token);
    }

    public boolean isLatestToken(String username, String token) {
        return token.equals(tokenMap.get(username));
    }

    public void invalidateToken(String username) {
        tokenMap.remove(username);
    }
}
