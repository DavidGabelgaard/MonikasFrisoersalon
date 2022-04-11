package com.example.monikasfrisoersalon;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;


public class GenerateMonth {
    private ArrayList<WorkDay> days;
    private DayOfWeek startDay;
    private int daysInTotalInLastMonth;




    public GenerateMonth(int year , int month) {

        startDay = LocalDate.of(year , month , 1).getDayOfWeek();

        int daysInCurrentMonth = YearMonth.of(year , month).lengthOfMonth();

        this.days = new ArrayList<>();

        if (month - 1 == 0) {
            daysInTotalInLastMonth = YearMonth.of(year - 1  , 12).lengthOfMonth();
        } else {
            daysInTotalInLastMonth = YearMonth.of(year , month - 1 ).lengthOfMonth();
        }

        for (int i = 1; i <= daysInCurrentMonth; i++) {

            LocalDate date = LocalDate.of(year , month , i);

            WorkDay workDay =  DBController.getWorkDayForSignedInUserWithDate(date);



            days.add(workDay);

        }
    }


    public ArrayList<WorkDay> getDays() {
        return days;
    }

    public DayOfWeek getStartDay() {
        return startDay;
    }

    public int getDaysInTotalInLastMonth() {
        return daysInTotalInLastMonth;
    }
}