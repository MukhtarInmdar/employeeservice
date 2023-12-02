package com.sumadhura.employeeservice.util;
import java.sql.Connection;
import java.sql.SQLException;

import oracle.jdbc.pool.OracleDataSource;

public class OracleCloudWalletExample {

    public static void main(String[] args) {
        try {
            OracleDataSource ods = new OracleDataSource();
            ods.setURL("jdbc:oracle:thin:@adb.ap-hyderabad-1.oraclecloud.com:1522/g211f4369d6f050_pmradb_tp.adb.oraclecloud.com");
            ods.setUser("ATPDB_OMSREE_UAT");
            ods.setPassword("AMS_OMSREE_db$1");

            // Configure Oracle Cloud Wallet
            ods.setConnectionProperties("");
//            ods.setConnectionProperties("oracle.net.wallet_location=(source=(method=file)(method_data=(directory=E:/AMS/Lead Generation/DB/Wallet_PMRADB_new_TLS)))");

            // Establish the connection
            try (Connection connection = ods.getConnection()) {
                // Use the connection as needed
                // ...

                // Close the connection
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
