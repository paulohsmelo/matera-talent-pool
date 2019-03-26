package com.paulomelo.materatalentpool.exception;

public class EmployeeNotFoundException extends RuntimeException {

    public EmployeeNotFoundException(Long id) {
        super("Not found employee with id " + id);
    }
}
