package com.paulomelo.materatalentpool.service;

import com.paulomelo.materatalentpool.exception.EmployeeNotFoundException;
import com.paulomelo.materatalentpool.model.Employee;
import com.paulomelo.materatalentpool.model.EmployeeStatus;
import com.paulomelo.materatalentpool.repository.EmployeeRepository;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;

public class EmployeeServiceTest {

    private EmployeeService service;

    private EmployeeRepository repository;

    @Rule
    public final ExpectedException expectException = ExpectedException.none();

    @Before
    public void setup() {
        repository = Mockito.mock(EmployeeRepository.class);
        service = new EmployeeService(repository);
    }

    @Test
    public void findAllActiveEmployessWithSuccess() {
        final Employee employee = new Employee();
        employee.setFirstName("Test 1");

        Mockito.when(repository.findByStatus(EmployeeStatus.ACTIVE)).thenReturn(Arrays.asList(employee));

        Assert.assertThat(service.findAllActiveEmployees(), Matchers.hasSize(1));
    }

    @Test
    public void findAllActiveEmployessEmptyResult() {
        Mockito.when(repository.findByStatus(EmployeeStatus.ACTIVE)).thenReturn(new ArrayList<>());

        Assert.assertThat(service.findAllActiveEmployees(), Matchers.empty());
    }

    @Test
    public void findActiveEmployeeByIdWithSuccess() {
        final Employee employee = new Employee().setFirstName("Test 1");

        Mockito.when(repository.findByIdAndStatus(1L, EmployeeStatus.ACTIVE)).thenReturn(employee);

        Assert.assertThat(service.findActiveEmployeeById(1L), Matchers.sameInstance(employee));
    }

    @Test
    public void findActiveEmployeeByWrongId() {
        final Employee employee = new Employee().setFirstName("Test 1");

        Mockito.when(repository.findByIdAndStatus(1L, EmployeeStatus.ACTIVE)).thenReturn(employee);

        expectException.expect(EmployeeNotFoundException.class);

        service.findActiveEmployeeById(2L);

        expectException.expectMessage("Not found employee with id  1");
    }

    @Test
    public void updateEmployeeWithSuccess() {
        final Employee employee = new Employee().setFirstName("Test 1");

        Mockito.when(repository.findByIdAndStatus(1L, EmployeeStatus.ACTIVE)).thenReturn(employee);
        Mockito.when(repository.save(employee)).thenReturn(employee);

        final Employee employeeUpdated = service.updateEmployee(1L, new Employee().setFirstName("Test 2"));

        Assert.assertThat(employeeUpdated.getFirstName(), Matchers.equalTo("Test 2"));
    }

    @Test
    public void updateEmployeeWithWrongId() {
        expectException.expect(EmployeeNotFoundException.class);

        service.updateEmployee(1L, null);

        expectException.expectMessage("Not found employee with id  1");
    }

    @Test
    public void deleteEmployeeWithSuccess() {
        final Employee employee = new Employee().setFirstName("Test 1");

        Mockito.when(repository.findByIdAndStatus(1L, EmployeeStatus.ACTIVE)).thenReturn(employee);

        service.deleteEmployee(1L);

        Assert.assertThat(employee.getStatus(), Matchers.equalTo(EmployeeStatus.INACTIVE));
    }

    @Test
    public void deleteEmployeeWithWrongId() {
        expectException.expect(EmployeeNotFoundException.class);

        service.deleteEmployee(1L);

        expectException.expectMessage("Not found employee with id  1");
    }
}
