package com.payfast;

import com.payfast.exceptions.InvalidRequestException;
import com.payfast.utils.XUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.ParseException;

public class XUtilsTest {

    @Test
    public void testValidateDate() throws InvalidRequestException, ParseException {
        // Test valid date
        String validDate = "14:50";
        String validFormat = "HH:mm";
        String validParamName = "time";
        String result = XUtils.validateDate(validDate, validFormat, validParamName);
        Assertions.assertEquals(validDate, result, "Valid date should be returned");

        // Test invalid date
        String invalidDate = "24:00"; // Invalid hour
        String invalidFormat = "HH:mm";
        String invalidParamName = "time";
        Assertions.assertThrows(InvalidRequestException.class, () -> {
            XUtils.validateDate(invalidDate, invalidFormat, invalidParamName);
        }, "InvalidRequestException should be thrown for invalid date");
    }


}

