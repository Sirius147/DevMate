package com.sirius.DevMate.setting;


import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;

import static org.assertj.core.api.Fail.fail;

public class DBconnectionTest {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testConnection() {
        try(Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/devmate?useSSL=false&serverTimezone=Asia/Seoul&characterEncoding=UTF-8",
                    "root",
                    "root"
        )){
            System.out.println(connection);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
}
