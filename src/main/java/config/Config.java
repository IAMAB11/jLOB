package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.IOException;

@Setter
@NoArgsConstructor
public class Config {

    public DatabaseConfig database;
    public RedisConfig redis;
    public LbankConfig lbank;

    public static Config fromPath(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Config config = mapper.readValue(new File(path), Config.class);
        
        // Override with environment variables if present
        if (System.getenv("DB_HOST") != null) {
            config.database.setHost(System.getenv("DB_HOST"));
        }
        if (System.getenv("DB_NAME") != null) {
            config.database.setName(System.getenv("DB_NAME"));
        }
        if (System.getenv("DB_PORT") != null) {
            try {
                config.database.setPort(Integer.parseInt(System.getenv("DB_PORT")));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid DB_PORT environment variable: must be a valid integer", e);
            }
        }
        if (System.getenv("DB_USER") != null) {
            config.database.setUsername(System.getenv("DB_USER"));
        }
        if (System.getenv("DB_PASSWORD") != null) {
            config.database.setPassword(System.getenv("DB_PASSWORD"));
        }
        if (System.getenv("REDIS_HOST") != null) {
            config.redis.setHost(System.getenv("REDIS_HOST"));
        }
        if (System.getenv("REDIS_PORT") != null) {
            try {
                config.redis.setPort(Integer.parseInt(System.getenv("REDIS_PORT")));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid REDIS_PORT environment variable: must be a valid integer", e);
            }
        }
        if (System.getenv("LBANK_API_KEY") != null) {
            if (config.lbank == null) {
                config.lbank = new LbankConfig();
            }
            config.lbank.setApiKey(System.getenv("LBANK_API_KEY"));
        }
        
        return config;
    }

    public DatabaseConfig getDatabaseConfig() {
        return database;
    }

    public RedisConfig getRedisConfig() {
        return redis;
    }

    public LbankConfig getLbankConfig() {
        return lbank;
    }
}

