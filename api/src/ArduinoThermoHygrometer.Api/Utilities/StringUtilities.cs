using System.Globalization;

namespace ArduinoThermoHygrometer.Api.Utilities;

public static class StringUtilities
{
    /// <summary>
    /// Capitalises the first letter of a string.
    /// </summary>
    /// <param name="str">The string to capitalize.</param>
    /// <returns>The string with the first letter capitalised.</returns>
    public static string? CapitaliseFirstLetter(string? str)
    {
        if (string.IsNullOrEmpty(str))
        {
            return null;
        }

        return $"{char.ToUpper(str[0], CultureInfo.InvariantCulture)}{str.Substring(1)}";
    }
}
