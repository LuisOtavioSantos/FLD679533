package com.example.rest.services.converters;

public class NumberConverter {

    public static Double convertToDouble(String strNumber) {
        if (strNumber == null) return 0D;
        String number = strNumber.replace(",", ".");
        return Double.parseDouble(number);
    }

    public static boolean isNotNumeric(String strNumber) {
        assert strNumber != null : "A string de entrada não pode ser nula";
        if (strNumber.isEmpty()) return true;

        String number = strNumber.replace(",", ".");
        return !number.matches("^[-+]?[0-9]*\\.?[0-9]+$"); // O '!' inverte o match
    }
}
