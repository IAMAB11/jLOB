package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Setter
@NoArgsConstructor
public class Config {

    // Environment variable names
    private static final String ENV_DB_HOST = "DB_HOST";
    private static final String ENV_DB_NAME = "DB_NAME";
    private static final String ENV_DB_PORT = "DB_PORT";
    private static final String ENV_DB_USER = "DB_USER";
    private static final String ENV_DB_PASSWORD = "DB_PASSWORD";
    private static final String ENV_REDIS_HOST = "REDIS_HOST";
    private static final String ENV_REDIS_PORT = "REDIS_PORT";

    // Immutable list of supported environment keys
    private static final List<String> SUPPORTED_ENVIRONMENT_KEYS = List.of(
        ENV_DB_HOST,
        ENV_DB_NAME,
        ENV_DB_PORT,
        ENV_DB_USER,
        ENV_DB_PASSWORD,
        ENV_REDIS_HOST,
        ENV_REDIS_PORT
    );

    public DatabaseConfig database;
    public RedisConfig redis;

    public static Config fromPath(String path) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Config config = mapper.readValue(new File(path), Config.class);
        
        // Override with environment variables if present
        if (System.getenv(ENV_DB_HOST) != null) {
            config.database.setHost(System.getenv(ENV_DB_HOST));
        }
        if (System.getenv(ENV_DB_NAME) != null) {
            config.database.setName(System.getenv(ENV_DB_NAME));
        }
        if (System.getenv(ENV_DB_PORT) != null) {
            try {
                config.database.setPort(Integer.parseInt(System.getenv(ENV_DB_PORT)));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid DB_PORT environment variable: must be a valid integer", e);
            }
        }
        if (System.getenv(ENV_DB_USER) != null) {
            config.database.setUsername(System.getenv(ENV_DB_USER));
        }
        if (System.getenv(ENV_DB_PASSWORD) != null) {
            config.database.setPassword(System.getenv(ENV_DB_PASSWORD));
        }
        if (System.getenv(ENV_REDIS_HOST) != null) {
            config.redis.setHost(System.getenv(ENV_REDIS_HOST));
        }
        if (System.getenv(ENV_REDIS_PORT) != null) {
            try {
                config.redis.setPort(Integer.parseInt(System.getenv(ENV_REDIS_PORT)));
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid REDIS_PORT environment variable: must be a valid integer", e);
            }
        }
        
        return config;
    }

    public DatabaseConfig getDatabaseConfig() {
        return database;
    }

    public RedisConfig getRedisConfig() {
        return redis;
    }

    public static List<String> getSupportedEnvironmentKeys() {
        return SUPPORTED_ENVIRONMENT_KEYS;
    }
}

