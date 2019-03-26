package com.paulomelo.materatalentpool.helper;

import com.paulomelo.materatalentpool.model.Employee;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class EmployeeDataHelper {

    public static List<Employee> createTwoEmployees() {
        final Employee employeeRick = new Employee()
                .setFirstName("RICK")
                .setLastName("SMITH")
                .setDateOfBirth(LocalDate.of(1985, 12, 14))
                .setDateOfEmployment(LocalDate.of(2016, 4, 14));

        final Employee employeeBob = new Employee()
                .setFirstName("BOB")
                .setMiddleInitial("MICHAEL")
                .setLastName("FORD")
                .setDateOfBirth(LocalDate.of(1989, 11, 23))
                .setDateOfEmployment(LocalDate.of(2015, 2, 25));

        return Arrays.asList(employeeRick, employeeBob);
    }

    public static Employee createOneEmployee() {
        return new Employee()
                .setFirstName("FERNANDO")
                .setMiddleInitial("MEDINA")
                .setLastName("DE MELO")
                .setDateOfBirth(LocalDate.of(1989, 11, 19))
                .setDateOfEmployment(LocalDate.of(2017, 6, 18));
    }
}
