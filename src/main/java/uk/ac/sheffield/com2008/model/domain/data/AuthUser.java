package uk.ac.sheffield.com2008.model.domain.data;

public record AuthUser(String passwordHash, String salt) {
}
