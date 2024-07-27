namespace ArduinoThermoHygrometer.Web.Exceptions;

public class BadRequestException : BaseException
{
    public BadRequestException() : base() { }

    public BadRequestException(string message) : base(message) { }

    public BadRequestException(string message, Exception innerException) : base(message, innerException) { }
}
