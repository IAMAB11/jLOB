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
        // Verify they are the same instance (since it's a static final)
        assertSame(keys1, keys2);
    }

    @Test
    public void testEnvironmentKeysAreImmutable() {
        List<String> keys = Config.getSupportedEnvironmentKeys();
        
        // Verify that the list is immutable by attempting to modify it
        assertThrows(UnsupportedOperationException.class, () -> {
            keys.add("NEW_KEY");
        });
        assertThrows(UnsupportedOperationException.class, () -> {
            keys.remove(0);
        });
        assertThrows(UnsupportedOperationException.class, () -> {
            keys.clear();
        });
    }
}
