package config;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigTest {

    @Test
    public void testGetSupportedEnvironmentKeys() {
        List<String> keys = Config.getSupportedEnvironmentKeys();
        
        assertNotNull(keys);
        assertEquals(7, keys.size());
        assertTrue(keys.contains("DB_HOST"));
        assertTrue(keys.contains("DB_NAME"));
        assertTrue(keys.contains("DB_PORT"));
        assertTrue(keys.contains("DB_USER"));
        assertTrue(keys.contains("DB_PASSWORD"));
        assertTrue(keys.contains("REDIS_HOST"));
        assertTrue(keys.contains("REDIS_PORT"));
    }

    @Test
    public void testEnvironmentKeysAreConsistent() {
        List<String> keys1 = Config.getSupportedEnvironmentKeys();
        List<String> keys2 = Config.getSupportedEnvironmentKeys();
        
        assertEquals(keys1, keys2);
    }
}
