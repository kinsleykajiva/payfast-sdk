package com.payfast.Services;


import com.payfast.HttpUtils;
import com.payfast.PayFast;
import com.payfast.exceptions.InvalidRequestException;

import java.text.ParseException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import static com.payfast.HttpUtils.convertMapToQueryString;

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
            // Validate.validateOptions(data, new String[]{"from", "date", "offset", "limit"});

            if (data[1] != null) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date toDate = dateFormat.parse(data[1].toString());
                if (toDate.before(currentDate)) {
                    queryParam = new String[]{"to=" + dateFormat.format(currentDate), "from=" + data[1]};
                } else {
                    throw new InvalidRequestException("To date should be before current date", 400);
                }
            }
            String queryParamString = String.join("&", queryParam);
            String url = PayFast.getApiUrl() + "/" + PATH + "?" + queryParamString;
            return HttpUtils.sendHttpGetRequest(url);

        } catch (ParseException e) {
            throw new InvalidRequestException("Date format is incorrect", 400);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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
    public String daily(Map<String,String> data) throws InvalidRequestException, Exception {
        if(!data.containsKey("daily")){
            throw new InvalidRequestException("Required \"date\" parameter missing", 400);
        }

        try {
            String queryString = PayFast.getApiUrl() + "/" +PATH + "/daily"+ convertMapToQueryString(data);
           return HttpUtils.sendHttpGetRequest(queryString);

        } catch (Exception e) {
            if (e.getMessage().contains("time:")) {
                throw new InvalidRequestException("Date format is incorrect", 400);
            }
            throw new RuntimeException(e);
        }
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
    public String weekly(Map<String,String> data) throws InvalidRequestException, Exception {
        if(!data.containsKey("date")){
            throw new InvalidRequestException("Required \"date\" parameter missing", 400);
        }
        try {
           // Validate.validateOptions(data, new String[]{"date", "offset", "limit"});
            String queryString = PayFast.getApiUrl() + "/" +PATH + "/weekly"+ convertMapToQueryString(data);
            return HttpUtils.sendHttpGetRequest(queryString);

           /* Request.sendApiRequest("GET", PATH + "/weekly", data);
            String queryString = PayFast.getApiUrl() + "/" +PATH + "/"+ convertMapToQueryString(data);
            return HttpUtils.sendHttpGetRequest(queryString);*/
        } catch (Exception e) {
            if (e.getMessage().contains("time:")) {
                throw new InvalidRequestException("Date format is incorrect", 400);
            }
            throw new RuntimeException(e);
        }

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
    public String monthly(Map<String,String> data) throws InvalidRequestException, Exception {
        if(!data.containsKey("date")){
            throw new InvalidRequestException("Required \"date\" parameter missing", 400);
        }
        try {
           // Validate.validateOptions(data, new String[]{"date", "offset", "limit"});
           // Request.sendApiRequest("GET", PATH + "/monthly", data);

            String queryString = PayFast.getApiUrl() + "/" +PATH + "/monthly"+ convertMapToQueryString(data);
            return HttpUtils.sendHttpGetRequest(queryString);

        } catch (Exception e) {
            if (e.getMessage().contains("time:")) {
                throw new InvalidRequestException("Date format is incorrect", 400);
            }
            throw new RuntimeException(e);
        }

    }
}

