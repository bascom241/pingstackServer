package com.example.pingstack.configuration;

import com.example.pingstack.seeders.DataSeeder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@Profile({"dev", "test"})
public class DatabaseSeeder implements CommandLineRunner {

    private final List<DataSeeder> seeders;


    public DatabaseSeeder(List<DataSeeder> seeders) {
        this.seeders = seeders;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🚀 Starting database seeding check...");

        // Sort seeders by their Order value and run them sequentially
        seeders.stream()
                .sorted(Comparator.comparingInt(DataSeeder::getOrder))
                .forEach(DataSeeder::seed);

        System.out.println("✅ Seeding process complete.");
    }
}
