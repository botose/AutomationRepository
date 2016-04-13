package com.automation.server.repository;

/**
 * Created by Elemer_Botos on 4/13/2016.
 */
public class GitExecutionException extends Exception{
    public GitExecutionException(String message) {
        super(message);
    }

    public GitExecutionException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
