package com.payfast.utils;

import com.payfast.exceptions.InvalidRequestException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XUtils {

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
        try {
            Date createDate = sdf.parse(date);
            if (createDate == null || !sdf.format(createDate).equals(date)) {
                throw new InvalidRequestException("Invalid date format for \"" + paramName + "\"", 400);
            }
        } catch (ParseException e) {
            throw new InvalidRequestException("Invalid date format for \"" + paramName + "\"", 400);
        }
        return date;
    }
}
