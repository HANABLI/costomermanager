package fr.nablihatem3.costomermanager.querry;
/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 11/08/2023
 */
public class UserQuery {
    public static final String INSERT_USER_QUERY = "INSERT INTO Users (first_name, last_name, email, password) VALUES (:firstName, :lastName, :email, :password)";
    public static final String COUNT_USER_EMAIL_QUERY  = "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String INSERT_ACCOUNT_VERIFICATION_URL_QUERY = "INSERT INTO AccountVerifications (user_id, url) VALUES (:userId, :url)";
}
