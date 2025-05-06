package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;

@Component
public class CsvLoader implements CommandLineRunner {

    private final JdbcTemplate jdbc;

    public CsvLoader(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(String... args) throws Exception {
        importCsvData("src/main/resources/csv/users.csv", "users");
        importCsvData("src/main/resources/csv/products.csv", "products");
        importCsvData("src/main/resources/csv/orders.csv", "orders");
    }

    private void importCsvData(String csvPath, String tableName) {
        String absoluteCsvPath = Paths.get(csvPath).toAbsolutePath().toString().replace("\\", "\\\\");

        String columns = getColumnsForTable(tableName);
        if (columns == null) {
            System.out.println("No columns mapping for table: " + tableName);
            return;
        }

        String sql = String.format(
                "LOAD DATA LOCAL INFILE '%s' INTO TABLE %s " +
                        "CHARACTER SET utf8mb4 FIELDS TERMINATED BY ',' " +
                        "OPTIONALLY ENCLOSED BY '\"' LINES TERMINATED BY '\\n' IGNORE 1 LINES (%s)",
                absoluteCsvPath, tableName, columns
        );

        try {
            jdbc.execute(sql);
            System.out.printf("Successfully imported data into %s from %s%n", tableName, csvPath);
        } catch (Exception e) {
            System.err.println("Error importing data into table " + tableName + ": " + e.getMessage());
        }
    }

    // Метод для получения колонок, которые соответствуют каждой таблице
    private String getColumnsForTable(String tableName) {
        switch (tableName) {
            case "users":
                return "id,username,email,password,role,rating,balance";
            case "products":
                return "id,seller_id,name,description,price,stock,image_url,created_at,category_id,category_code";
            case "orders":
                return "id,buyer_id,seller_id,product_id,quantity,total_price,status,order_date";
            default:
                return null;
        }
    }
}
