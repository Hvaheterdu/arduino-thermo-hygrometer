package api.arduinothermohygrometer.model;

public record IssuedToken(String accessToken, String tokenType, long expiresIn) {}
