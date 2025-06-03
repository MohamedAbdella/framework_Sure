//package com.sure.utilities;
//
//import com.sure.configuration.ConfigManager;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//public class Database {
//
//    static Connection connection = null;
//    static ConfigManager config = new ConfigManager();
//
//    static String DB_url = config.getProperty("...............");
//    static String DB_user_name = config.getProperty(".............");
//    static String DB_password = config.getProperty(".............");
//
//    public static void setUpConnection() {
//        if (connection == null) {
//            try {
//                Class.forName("com.mysql.cj.jdbc.Driver");
//                String unicode="useSSL=false&autoReconnect=true&useUnicode=yes&characterEncoding=UTF-8";
//                connection = DriverManager.getConnection(DB_url +"?"+unicode, DB_user_name, DB_password);
//            } catch (ClassNotFoundException | SQLException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            System.out.println("------------------- Database Connection Opened --------------------------");
//        }
//
//    }
//
//
//    public static synchronized List<HashMap<String, String>> getSqlResultMap(String query){
//
//        ArrayList<HashMap<String, String>> record = null;
//        int j = 0;
//        boolean found = false;
//        while (j <10) {
//            record = getRecords(query);
//            String key = (String) record.get(0).keySet().toArray()[0];
//            if (record.get(0).get(key) == null) {
//                try {
//                    Thread.sleep(20000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//
//            }else {
//                found = true;
//                System.out.println(" +++++ Get it records ++++++");
//                break;
//            }
//            j++;
//        }
//        LoggerClass.logInfo("The Database records are {}", record);
//
//        return  record;
//    }
//
//    public static synchronized ArrayList<HashMap<String, String>> getRecords(String query)  {
//
//        ArrayList<HashMap<String, String>> resultSetWithMetaData = new ArrayList<>();
//
//        try {
//
//            Statement statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery(query);
//            ResultSetMetaData metaData = resultSet.getMetaData();
//
//            while (resultSet.next()) {
//                HashMap<String, String> dataMap = new HashMap<>();
//                for (int i = 1;i<=metaData.getColumnCount() ; i++) {
//
//                    dataMap.put(metaData.getColumnName(i).toLowerCase(), resultSet.getString(i));
//                }
//                resultSetWithMetaData.add(dataMap);
//            }
//
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//
//
//
//        return resultSetWithMetaData;
//
//    }
//
//
//    // ADD - update - Delete  Data in DB
//    public static void add_update_delete_Data (String query) throws  SQLException {
//        Statement statement = connection.createStatement();
//        statement.executeUpdate(query);
//
//    }
//
//
//
//    public static void closeConnection() {
//
//        try {
//            if (connection != null)
//                connection.close();
//            connection = null;
//            System.out.println("------------------ Database Connection Closed -------------------------");
//
//        } catch (SQLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//}
