package api.arduinothermohygrometer.exceptions;

public class ResourceMappingFailedException extends RuntimeException {
    public ResourceMappingFailedException(String message) {
        super(message);
    }
}
