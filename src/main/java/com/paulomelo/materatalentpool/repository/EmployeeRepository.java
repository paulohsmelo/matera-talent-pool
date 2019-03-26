package com.paulomelo.materatalentpool.repository;

import com.paulomelo.materatalentpool.model.Employee;
import com.paulomelo.materatalentpool.model.EmployeeStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    List<Employee> findByStatus(final EmployeeStatus status);

    Employee findByIdAndStatus(final Long id, final EmployeeStatus status);
}
