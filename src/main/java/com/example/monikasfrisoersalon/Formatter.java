package com.example.monikasfrisoersalon;


import java.time.LocalDate;

public class Formatter {

    public static String formatUserName(String username) {
        return username.replaceAll("@localhost" ,"");
    }
    public static String bigFirstLetter(String name) {
        String temp = name.substring(0 , 1 ).toUpperCase();

        return temp + name.substring(1);




    }

    public static String getMonthFromInt(int month) {
        return switch (month) {
            case 1 -> "Januar";
            case 2 -> "Februar";
            case 3 -> "Marts";
            case 4 -> "April";
            case 5 -> "Maj";
            case 6 -> "Juni";
            case 7 -> "Juli";
            case 8 -> "August";
            case 9 -> "September";
            case 10 -> "Oktober";
            case 11 -> "November";
            case 12 -> "December";
            default -> null;
        };
    }


    public static String getDayFromLocalDate(LocalDate localDate) {

        return switch (localDate.getDayOfWeek()) {
            case MONDAY -> "Mandag";
            case TUESDAY -> "Tirsdag";
            case WEDNESDAY -> "Onsdag";
            case THURSDAY -> "Torsdag";
            case FRIDAY -> "Fredag";
            case SUNDAY -> "Lørdag";
            case SATURDAY -> "Søndag";
        };
    }


}
