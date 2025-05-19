package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class CsvLoader implements CommandLineRunner {

    private final JdbcTemplate jdbc;

    public CsvLoader(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public void run(String... args) {
        loadCsvWithLogging("src/main/resources/csv/users.csv", "users");
        loadCsvWithLogging("src/main/resources/csv/products_for_db.csv", "products");
        loadCsvWithLogging("src/main/resources/csv/orders.csv", "orders");
    }

    private void loadCsvWithLogging(String csvPath, String tableName) {
        System.out.printf("⏳ Attempting to import CSV file for table '%s' from: %s%n", tableName, csvPath);

        Path path = Paths.get(csvPath);
        if (!Files.exists(path)) {
            System.err.printf("❌ File not found: %s%n", path.toAbsolutePath());
            return;
        }

        try {
            importCsvData(path.toAbsolutePath().toString(), tableName);
        } catch (Exception e) {
            System.err.printf("❌ Failed to import data for table '%s': %s%n", tableName, e.getMessage());
        }
    }

    private void importCsvData(String absoluteCsvPath, String tableName) {
        String formattedPath = absoluteCsvPath.replace("\\", "\\\\");
        String columns = getColumnsForTable(tableName);

        if (columns == null) {
            System.out.println("⚠️ No columns mapping for table: " + tableName);
            return;
        }

        // Optional: Clear table before import
        try {
            jdbc.execute("TRUNCATE TABLE " + tableName);
            System.out.printf("🧹 Table '%s' truncated before import.%n", tableName);
        } catch (Exception e) {
            System.err.printf("⚠️ Could not truncate table '%s': %s%n", tableName, e.getMessage());
        }

        String sql = String.format(
                "LOAD DATA LOCAL INFILE '%s' INTO TABLE %s " +
                        "CHARACTER SET utf8mb4 " +
                        "FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '\"' " +
                        "LINES TERMINATED BY '\n' IGNORE 1 LINES (%s)",
                formattedPath, tableName, columns
        );

        try {
            jdbc.execute(sql);
            System.out.printf("✅ Successfully imported data into '%s' from '%s'%n", tableName, absoluteCsvPath);
        } catch (Exception e) {
            System.err.printf("❌ Error importing data into table '%s': %s%n", tableName, e.getMessage());
        }
    }

    private String getColumnsForTable(String tableName) {
        switch (tableName) {
            case "users":
                return "id,username,email,password,role,rating,balance";
            case "products":
                // Adjusted to match CSV: removed 'description' column
                return "seller_id,name,price,stock,image_url,created_at,category_id,category_code";
            case "orders":
                return "id,buyer_id,seller_id,product_id,quantity,total_price,status,order_date";
            default:
                return null;
        }
    }
}