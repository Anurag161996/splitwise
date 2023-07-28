package com.setu.splitwise.utils;

import java.text.DecimalFormat;

public class ExpenseUtils {
    public static double roundToTwoDecimalPlaces(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(value));
    }
}
