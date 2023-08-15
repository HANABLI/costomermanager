package fr.nablihatem3.costomermanager.enumeration;
/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 11/08/2023
 */
public enum VerificationType {
    ACCOUNT("ACCOUNT"),
    PASSWORD("PASSWORD");

    private final String type;

    VerificationType(String type) { this.type = type; }

    public String getType() {
        return this.type.toLowerCase();
    }
}
