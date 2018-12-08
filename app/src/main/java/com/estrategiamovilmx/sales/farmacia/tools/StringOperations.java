package com.estrategiamovilmx.sales.farmacia.tools;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by administrator on 10/07/2017.
 */
public class StringOperations {
    private static final String TAG = StringOperations.class.getSimpleName();
    public static String getStringBeforeDelimiter(String original, String delimiter){
        String value = "";

        int index = original.indexOf(delimiter);
        value = original.substring(0,index);
        return value;
    }
    public static String getAmountFormat(String amount){
        if (amount!=null && amount != "") {
            NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
            return format.format(Double.parseDouble(amount));
        }else{
            return null;
        }
    }
    public static String getAmountFormatWithNoDecimals(String amount){
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("es", "MX"));
        format.setMaximumFractionDigits(0);
        return format.format(Double.parseDouble(amount));
    }
    public static String getStringWithCashSymbol(String amount){
        return "$ "+amount;
    }
    public static String getPercentageFormat(String number){
        return number.concat(" %");
    }
    public static String getStringWithDe(String s){return "De: " + s + " A Solo";}
    public static String getStringWithA(String s){return "" + s;}
    public static String getDecimalFormat(String amount){
        DecimalFormat df = new DecimalFormat();
        Float number = 0f;
        try {
            number = Float.parseFloat(amount);
            df.setMaximumFractionDigits(2);
        }catch(NumberFormatException e){
            return "0.00";
        }
        return df.format(number);
    }
    public static String getDecimalFormatWithNoDecimals(String amount){
        DecimalFormat df = new DecimalFormat();
        Float number = 0f;
        try {
            number = Float.parseFloat(amount);
            df.setMaximumFractionDigits(0);
        }catch(NumberFormatException e){
            return "0.00";
        }
        return df.format(number);
    }

    public static Date getDate(String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            Date d = sdf.parse(date);
            return d;
        } catch (ParseException e) {
            return null;
        }
    }

}
