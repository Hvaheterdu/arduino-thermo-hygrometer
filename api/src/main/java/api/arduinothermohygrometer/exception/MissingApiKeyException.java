package api.arduinothermohygrometer.exception;

import org.springframework.security.core.AuthenticationException;

public class MissingApiKeyException extends AuthenticationException {
  public MissingApiKeyException() {
    super("Missing API key.");
  }
}
