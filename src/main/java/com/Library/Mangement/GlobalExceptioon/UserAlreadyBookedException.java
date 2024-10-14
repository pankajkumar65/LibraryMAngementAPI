package com.Library.Mangement.GlobalExceptioon;

public class UserAlreadyBookedException extends RuntimeException {

    public UserAlreadyBookedException(String message) {
        super(message);
    }
}

