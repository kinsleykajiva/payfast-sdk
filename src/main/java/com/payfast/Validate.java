package com.payfast;

import com.payfast.exceptions.InvalidRequestException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

public class Validate {

    /**
     * Validate Date
     *
     * validateDate("14:50", "HH:mm")
     *
     * @param date
     * @param format
     * @param paramName
     * @return
     * @throws InvalidRequestException
     * @throws ParseException
     */
    public static String validateDate(String date, String format, String paramName)
            throws InvalidRequestException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setLenient(false);
        Date createDate = sdf.parse(date);
        if (createDate == null || !sdf.format(createDate).equals(date)) {
            throw new InvalidRequestException("Invalid date format for \"" + paramName + "\"", 400);
        }
        return date;
    }
    /**
     * Validate integers
     *
     * @param value
     * @param key
     * @throws InvalidRequestException
     */
    private static void validateInt(String value, String key)
            throws InvalidRequestException {
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new InvalidRequestException("Invalid format for \"" + key + "\"", 400);
        }
    }
    /**
     * Validate account type
     *
     * @param value
     * @param key
     * @throws InvalidRequestException
     */
    private static void validateAccType(String value, String key)
            throws InvalidRequestException {
        String[] validAccTypes = {"current", "savings"};
        if (!Arrays.asList(validAccTypes).contains(value)) {
            throw new InvalidRequestException("Invalid format for \"" + key + "\"", 400);
        }
    }

    /**
     * Remove items from an array if they are not in the allowed list
     *
     * @param array
     * @param validation
     * @throws InvalidRequestException
     * @throws ParseException
     */
    public static void validateOptions(Map<String, String> array, Map<String, String> validation)
            throws InvalidRequestException, ParseException {
        try {
            for (Map.Entry<String, String> entry : array.entrySet()) {
                String attribute = entry.getKey();
                String value = entry.getValue();
                if (validation.containsKey(attribute)) {
                    switch (validation.get(attribute)) {
                        case "int":
                            validateInt(value, attribute);
                            break;
                        case "date":
                            validateDate(value, "yyyy-MM-dd", attribute);
                            break;
                        case "monthly":
                            validateDate(value, "yyyy-MM", attribute);
                            break;
                        case "accType":
                            validateAccType(value, attribute);
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (InvalidRequestException e) {
            throw new InvalidRequestException(e.getMessage(), 400);
        }
    }

}
