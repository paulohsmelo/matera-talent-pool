package com.paulomelo.materatalentpool.service;

import com.paulomelo.materatalentpool.exception.EmployeeNotFoundException;
import com.paulomelo.materatalentpool.model.Employee;
import com.paulomelo.materatalentpool.model.EmployeeStatus;
import com.paulomelo.materatalentpool.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> findAllActiveEmployees() {
        return employeeRepository.findByStatus(EmployeeStatus.ACTIVE);
    }

    public Employee findActiveEmployeeById(final Long id) {
        final Employee employee = employeeRepository.findByIdAndStatus(id, EmployeeStatus.ACTIVE);
        if (isNull(employee)) {
            throw new EmployeeNotFoundException(id);
        }
        return employee;
    }

    public Employee createEmployee(final Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(final Long id, final Employee employee) {
        final Employee existentEmployee = findActiveEmployeeById(id)
                .setFirstName(employee.getFirstName())
                .setMiddleInitial(employee.getMiddleInitial())
                .setLastName(employee.getLastName())
                .setDateOfBirth(employee.getDateOfBirth())
                .setDateOfEmployment(employee.getDateOfEmployment());

        return employeeRepository.save(existentEmployee);
    }

    public void deleteEmployee(final Long id) {
        final Employee employee = findActiveEmployeeById(id);
        employee.inactive();
        employeeRepository.save(employee);
    }
}
