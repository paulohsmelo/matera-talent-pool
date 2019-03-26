package com.paulomelo.materatalentpool.controller;

import com.paulomelo.materatalentpool.converter.EmployeeConverter;
import com.paulomelo.materatalentpool.dto.EmployeeRequestDTO;
import com.paulomelo.materatalentpool.exception.EmployeeNotFoundException;
import com.paulomelo.materatalentpool.model.Employee;
import com.paulomelo.materatalentpool.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Api(value = "Employees", description = "API to manipulate employees")
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeConverter employeeConverter;

    public EmployeeController(final EmployeeService employeeService, final EmployeeConverter employeeConverter) {
        this.employeeService = employeeService;
        this.employeeConverter = employeeConverter;
    }

    @ApiOperation(value = "Returns all active employees")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all active employees or an empty list, if no one were found", response = Employee.class, responseContainer = "List"),
            @ApiResponse(code = 500, message = "General error")
        }
    )
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Iterable<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.findAllActiveEmployees());
    }

    @ApiOperation(value = "Returns an especific active employee with given id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "An active employee with given id"),
            @ApiResponse(code = 404, message = "Didn't found an employee with given id and status active"),
            @ApiResponse(code = 500, message = "General error")
        }
    )
    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> getEmployee(@PathVariable("id") final Long id) {
        try {
            return ResponseEntity.ok(employeeService.findActiveEmployeeById(id));
        } catch (EmployeeNotFoundException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage());
        }
    }

    @ApiOperation(value = "Creates a new active employee")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "New active employee created successfully"),
            @ApiResponse(code = 500, message = "General error")
        }
    )
    @PostMapping(produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Employee> createEmployee(@RequestBody @Valid final EmployeeRequestDTO employeeRequestDTO) {
        final Employee employeeRequest = employeeConverter.convert(employeeRequestDTO);

        final Employee employeeCreated = employeeService.createEmployee(employeeRequest);

        final URI uri = URI.create("/employee/" + employeeCreated.getId());
        return ResponseEntity.created(uri).body(employeeCreated);
    }

    @ApiOperation(value = "Updates the employee with given id. It's not possible to change the employee status via this api. To inactivate an employee, should be used the delete endpoint")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Employee updated successfully"),
            @ApiResponse(code = 404, message = "Didn't found an employee with given id and status active"),
            @ApiResponse(code = 500, message = "General error")
        }
    )
    @PutMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") final Long id, @RequestBody final EmployeeRequestDTO employeeRequestDTO) {
        try {
            final Employee employee = employeeConverter.convert(employeeRequestDTO);
            return ResponseEntity.ok(employeeService.updateEmployee(id, employee));
        } catch (EmployeeNotFoundException ex) {
            throw new ResponseStatusException(NOT_FOUND, ex.getMessage());
        }
    }

    @ApiOperation(value = "Inactivate the employee with given id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Employee inactivated successfully"),
            @ApiResponse(code = 404, message = "Didn't found an employee with given id and status active"),
            @ApiResponse(code = 500, message = "General error")
        }
    )
    @ResponseStatus(NO_CONTENT)
    @DeleteMapping(path = "/{id}")
    public ResponseEntity deleteEmployee(@PathVariable("id") final Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.noContent().build();
        } catch (EmployeeNotFoundException ex) {
            throw new ResponseStatusException(NOT_FOUND, ex.getMessage());
        }
    }

}
