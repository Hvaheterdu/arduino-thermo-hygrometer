namespace ArduinoThermoHygrometer.Web.Exceptions;

public class ForbiddenException : BaseException
{
    public ForbiddenException() : base() { }

    public ForbiddenException(string message) : base(message) { }

    public ForbiddenException(string message, Exception innerException) : base(message, innerException) { }
}
