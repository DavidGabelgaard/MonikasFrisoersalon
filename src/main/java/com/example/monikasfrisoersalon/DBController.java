package com.example.monikasfrisoersalon;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.lang.String;
import java.util.ArrayList;

//test
public class DBController {

    private static Connection connection;

    public static int  connectToDatabase(java.lang.String username , java.lang.String password ) {
        java.lang.String url = "jdbc:mysql://localhost:3306";
            try {
                connection = DriverManager.getConnection(url, username.toLowerCase(), password);
            } catch (SQLException e) {
                if (e.getErrorCode() == 1045) {
                    return 1;
                } else if (e.getErrorCode() == 0) {
                    return 2;
                }
            }
            return 0;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertOpeningHoursWithDate(LocalDate date , LocalTime openingTime , LocalTime closingTime) {

    java.lang.String mySQL ="INSERT INTO  skema.opening_hours  (currentDate , openingHour , closingHour ) VALUES ('" + date + "','" +
        openingTime + "','" + closingTime + "')" ;
        try {
            Statement statement = connection.createStatement();

            statement.execute(mySQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static WorkDay getOpeningHoursWithDate(LocalDate date) {

        java.lang.String mySQL = "SELECT * FROM skema.opening_hours where currentDate = " + "'" + date + "'";

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(mySQL);

            if (!resultSet.next()) {

                java.lang.String tempSQLString = "SELECT * FROM  skema.standardopeninghours";

                resultSet = statement.executeQuery(tempSQLString);

                resultSet.next();

                WorkDay workDay = new WorkDay(
                        date,
                        resultSet.getTime(1).toLocalTime(),
                        resultSet.getTime(2).toLocalTime()
                );
                resultSet.close();
                return  workDay;

            } else {
                WorkDay workDay = new WorkDay(
                        date,
                        resultSet.getTime(2).toLocalTime(),
                        resultSet.getTime(3).toLocalTime()
                );
                resultSet.close();
                return  workDay;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;

    }

    public static WorkDay getWorkDayForSignedInUserWithDate(LocalDate date) {

        WorkDay workDay = getOpeningHoursWithDate(date);

        String activeUser = Formatter.formatUserName(getActiveUser());


        String mySQL = " SELECT * FROM skema." + activeUser + " Where currentDate = '" + date + "'" ;

        try {
            Statement statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(mySQL);

            if (!resultSet.next()) {
                return workDay;
            } else {
                ShiftTime shiftTime = new ShiftTime(
                        getActiveWorker(),
                        resultSet.getTime(3).toLocalTime(),
                        resultSet.getTime(4).toLocalTime()
                );
                if (workDay != null)
                    workDay.addShiftTime(shiftTime);
            }
            resultSet.close();

            mySQL = "SELECT  * FROM orders.monika WHERE orderDate = '" + date + "'";

            resultSet = statement.executeQuery(mySQL);

            if (!resultSet.next()) {
                return  workDay;
            } else {

                do {
                    String[] temp = resultSet.getString(9).split(",");



                    ArrayList<Treatments> treatments = new ArrayList<>();
                    for (String s : temp ) {
                       int i =  Integer.parseInt(s);
                        Treatments t = getTreatments(i);
                        treatments.add(t);
                    }



                    Orders orders = new Orders(
                        resultSet.getTime(3).toLocalTime(),
                        Duration.ofSeconds(resultSet.getTime(4).toLocalTime().toSecondOfDay()),
                        treatments,
                        getActiveWorker()
                    );

                    if (workDay != null) {
                        workDay.addOrders(orders);

                    }
                } while (resultSet.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workDay;
    }

    public static  void createShift(Worker worker , LocalDate date , LocalTime startTime , LocalTime endTime) {

        String mySQL = "INSERT INTO skema." + worker.getUserName() + "(currentDate, startTime, endTime) VALUE" + "('" + date + "','" +
                startTime + "','" + endTime + "')";

        try {
            Statement statement = connection.createStatement();

            statement.execute(mySQL);

        } catch (SQLException e) {
            e.printStackTrace();
        }



    }

    public static void changeShift(String string, LocalDate date , LocalTime startTime , LocalTime endTime) {

    }

    public static void deleteShift() {

    }

    public static String getActiveUser() {

        String mySQL = "SELECT USER(),CURRENT_USER()";

        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(mySQL);
            if (resultSet.next()) {


                return resultSet.getString(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "Error";

    }

    public static Worker getActiveWorker() {

        String mySQL = "SELECT * FROM employees.employees where username = '" + Formatter.formatUserName(getActiveUser()) + "'";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(mySQL);
            if (resultSet.next()) {
                return new Worker(
                        resultSet.getString(1),
                        resultSet.getString(2)
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Treatments getTreatments(int id) {

        String mySQL = "SELECT * FROM treatments.treatments where Id = '" + id + "'";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(mySQL);
            if (resultSet.next()) {
                return new Treatments(
                        id,
                        resultSet.getString(2),
                        resultSet.getInt(3),
                         resultSet.getTime(4).toLocalTime(),
                        resultSet.getString(5)
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;




    }


    public static Orders getOrderFromIdFromSignedInUser(int id) {



        return null;
    }
    public static Orders getOrderFromDateAndTimeFromSignedInUser(LocalDate date , LocalTime time) {
        // This only works since no 2 orders should have same start time
        // And the hairdresser is the same since each hairdresser have their respected tables


        return null;
    }

    public static ArrayList<Orders> getAllOrdersFromDate(LocalDate date) {
        // you need first to get all the hairdressers from the employees database and check each
        // Table with that employee name



        return null;
    }


    public static void createOrder(Orders orders) {

    }

    public static void changeExistingOrder() {
        // Don't do this one yet
    }

    public static void addTreatment (Treatments treatments) {

    }








}
