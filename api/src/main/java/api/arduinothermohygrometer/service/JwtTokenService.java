package api.arduinothermohygrometer.service;

import api.arduinothermohygrometer.model.IssuedToken;

public interface JwtTokenService {
  IssuedToken issueToken();
}
