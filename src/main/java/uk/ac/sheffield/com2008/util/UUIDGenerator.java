package uk.ac.sheffield.com2008.util;

import java.util.UUID;

public class UUIDGenerator {
    public static String generate() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
