package org.bbqqvv.backendeducation.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    // General Errors
    GENERAL_ERROR(9999, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),

    // 1000 Series - User Errors
    USER_NOT_FOUND(1001, "User not found with ID: {id}", HttpStatus.NOT_FOUND),
    ACCOUNT_NOT_FOUND(1002, "Account not found", HttpStatus.NOT_FOUND),
    INVALID_USER_CREDENTIALS(1003, "Invalid username or password", HttpStatus.UNAUTHORIZED),
    USER_ACCOUNT_LOCKED(1004, "Account is locked for user with ID: {id}", HttpStatus.FORBIDDEN),
    USER_UNAUTHORIZED(1005, "User is not authorized", HttpStatus.UNAUTHORIZED),
    USER_ACCESS_DENIED(1006, "Access denied for user with ID: {id}", HttpStatus.FORBIDDEN),
    USER_EXISTED(1007, "User existed", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(1008, "Email existed", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(1009, "Wrong password", HttpStatus.BAD_REQUEST),
    // 5000 Series - Account Errors
    ACCOUNT_DISABLED(5001, "Account is disabled", HttpStatus.FORBIDDEN),
    // 7000 Series - Validation Errors
    INVALID_DOB(7001, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_FUNDS(7002, "Insufficient funds", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(7003, "You do not have permission", HttpStatus.FORBIDDEN),
    REMOVE_FAVOURITE_NOT_FOUND(7004, "Favourite not found for user with ID: {userId} and product with ID: {productId}", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_FAVOURITE(7005, "Product with ID: {productId} is already in the favourites list of user with ID: {userId}", HttpStatus.BAD_REQUEST),

    // Uncategorized Errors
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    public String getMessageWithParams(Object... params) {
        return String.format(this.message, params);
    }
}
