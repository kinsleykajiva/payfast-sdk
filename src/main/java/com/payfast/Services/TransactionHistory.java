package com.payfast.Services;


import com.payfast.Validate;
import com.payfast.exceptions.InvalidRequestException;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
public class TransactionHistory {

    private static final String PATH = "transactions/history";

    /**
     * Transaction history
     * $payfast->transactionHistory->range(['from' => '2020-08-01', 'to' => '2020-08-07', 'offset' => 0, 'limit' => 1000]);
     *
     * @param data
     * @return String
     * @throws InvalidRequestException
     * @throws Exception
     */
    public String range(Object[] data) throws InvalidRequestException, Exception {
        String[] queryParam = new String[0];
        Date currentDate = new Date();
        if (!data[0].equals("from")) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            data[0] = dateFormat.format(currentDate);
        }
        try {
            Validate.validateOptions(data, new String[]{"from", "date", "offset", "limit"});
            if (data[1] != null) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date toDate = dateFormat.parse(data[1].toString());
                if (toDate.before(currentDate)) {
                    queryParam = new String[]{"to=" + dateFormat.format(currentDate), "from=" + data[1]};
                }
            }
            Request.sendApiRequest("GET", PATH, queryParam);
        } catch (Exception e) {
            if (e.getMessage().contains("time:")) {
                throw new InvalidRequestException("Date format is incorrect", 400);
            }
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Daily transaction history
     * $payfast->transactionHistory->daily(['date' => '2020-08-07', 'offset' => 0, 'limit' => 1000]);
     *
     * @param data
     * @return String
     * @throws InvalidRequestException
     * @throws Exception
     */
    public String daily(Object[] data) throws InvalidRequestException, Exception {
        if (!data[0].equals("date")) {
            throw new InvalidRequestException("Required \"date\" parameter missing", 400);
        }
        try {
            Validate.validateOptions(data, new String[]{"date", "offset", "limit"});
            Request.sendApiRequest("GET", PATH + "/daily", data);
        } catch (Exception e) {
            if (e.getMessage().contains("time:")) {
                throw new InvalidRequestException("Date format is incorrect", 400);
            }
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Weekly transaction history
     * $payfast->transactionHistory->weekly(['date' => '2020-08-07', 'offset' => 0, 'limit' => 1000]);
     *
     * @param data
     * @return String
     * @throws InvalidRequestException
     * @throws Exception
     */
    public String weekly(Object[] data) throws InvalidRequestException, Exception {
        if (!data[0].equals("date")) {
            throw new InvalidRequestException("Required \"date\" parameter missing", 400);
        }
        try {
            Validate.validateOptions(data, new String[]{"date", "offset", "limit"});
            Request.sendApiRequest("GET", PATH + "/weekly", data);
        } catch (Exception e) {
            if (e.getMessage().contains("time:")) {
                throw new InvalidRequestException("Date format is incorrect", 400);
            }
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * Monthly transaction history
     * $payfast->transactionHistory->monthly(['date' => '2020-08', 'offset' => 0, 'limit' => 1000]);
     *
     * @param data
     * @return String
     * @throws InvalidRequestException
     * @throws Exception
     */
    public String monthly(Object[] data) throws InvalidRequestException, Exception {
        if (!data[0].equals("date")) {
            throw new InvalidRequestException("Required \"date\" parameter missing", 400);
        }
        try {
            Validate.validateOptions(data, new String[]{"date", "offset", "limit"});
            Request.sendApiRequest("GET", PATH + "/monthly", data);
        } catch (Exception e) {
            if (e.getMessage().contains("time:")) {
                throw new InvalidRequestException("Date format is incorrect", 400);
            }
            throw new RuntimeException(e);
        }
        return null;
    }
}
}
