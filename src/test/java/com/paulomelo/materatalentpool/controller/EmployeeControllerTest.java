package com.paulomelo.materatalentpool.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paulomelo.materatalentpool.exception.EmployeeNotFoundException;
import com.paulomelo.materatalentpool.helper.EmployeeDataHelper;
import com.paulomelo.materatalentpool.model.Employee;
import com.paulomelo.materatalentpool.repository.EmployeeRepository;
import com.paulomelo.materatalentpool.service.EmployeeService;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.nio.charset.Charset;

import static com.paulomelo.materatalentpool.helper.EmployeeDataHelper.createOneEmployee;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration(exclude = {HibernateJpaAutoConfiguration.class})
@SpringBootTest
public class EmployeeControllerTest {

    private static final String API_GET_ALL_EMPLOYEES = "/employees";
    private static final String API_GET_EMPLOYEE_BY_ID = "/employees/{id}";
    private static final String API_CREATE_EMPLOYEE = "/employees";
    private static final String API_UPDATE_EMPLOYEE = "/employees/{id}";
    private static final String API_DELETE_EMPLOYEE = "/employees/{id}";

    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EmployeeService service;

    @MockBean
    private EmployeeRepository repository;

    @Autowired
    private EmployeeController controller;

    @Before
    public void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void listAllEmployees() throws Exception {
        when(service.findAllActiveEmployees()).thenReturn(EmployeeDataHelper.createTwoEmployees());

        final MockHttpServletResponse response = mvc.perform(get(API_GET_ALL_EMPLOYEES)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), equalTo(OK.value()));
        JSONAssert.assertEquals(response.getContentAsString(), parseJSON("list-all-employees-response.json"), JSONCompareMode.STRICT);
    }

    @Test
    public void listEmployeeById() throws Exception {
        when(service.findActiveEmployeeById(1L)).thenReturn(createOneEmployee());

        final MockHttpServletResponse response = mvc.perform(get(API_GET_EMPLOYEE_BY_ID, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), equalTo(OK.value()));
        JSONAssert.assertEquals(response.getContentAsString(), parseJSON("list-employee-by-id-response.json"), JSONCompareMode.STRICT);
    }

    @Test
    public void listEmployeeByWrongId() throws Exception {
        when(service.findActiveEmployeeById(1L)).thenThrow(new EmployeeNotFoundException(1L));

        final MockHttpServletResponse response = mvc.perform(get(API_GET_EMPLOYEE_BY_ID, 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), equalTo(NOT_FOUND.value()));
    }

    @Test
    public void createEmployee() throws Exception {
        final Employee employee = createOneEmployee();
        when(service.createEmployee(any(Employee.class))).thenReturn(employee);

        final MockHttpServletResponse response = mvc.perform(post(API_CREATE_EMPLOYEE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody(employee)))
                .andReturn()
                .getResponse();

        assertThat(response.getHeader("Location"), notNullValue());
        assertThat(response.getStatus(), equalTo(HttpStatus.CREATED.value()));
        JSONAssert.assertEquals(response.getContentAsString(), parseJSON("create-employee-response.json"), JSONCompareMode.STRICT);
    }

    @Test
    public void updateEmployee() throws Exception {
        when(service.updateEmployee(anyLong(), any(Employee.class))).thenReturn(new Employee().setFirstName("PETER"));

        final MockHttpServletResponse response = mvc.perform(put(API_UPDATE_EMPLOYEE, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody(createOneEmployee())))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), equalTo(OK.value()));
        JSONAssert.assertEquals(response.getContentAsString(), parseJSON("update-employee-response.json"), JSONCompareMode.STRICT);
    }

    @Test
    public void updateEmployeeWithWrongId() throws Exception {
        when(service.updateEmployee(anyLong(), any(Employee.class))).thenThrow(EmployeeNotFoundException.class);

        final MockHttpServletResponse response = mvc.perform(put(API_UPDATE_EMPLOYEE, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody(createOneEmployee())))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), equalTo(NOT_FOUND.value()));
    }

    @Test
    public void deleteEmployee() throws Exception {
        doNothing().when(service).deleteEmployee(1L);

        final MockHttpServletResponse response = mvc.perform(delete(API_DELETE_EMPLOYEE, 1))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), equalTo(NO_CONTENT.value()));
        assertThat(response.getContentAsString(), equalTo(""));
    }

    @Test
    public void deleteEmployeeWithWrongId() throws Exception {
        doThrow(EmployeeNotFoundException.class).when(service).deleteEmployee(1L);

        final MockHttpServletResponse response = mvc.perform(delete(API_DELETE_EMPLOYEE, 1))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus(), equalTo(NOT_FOUND.value()));
    }

    private String requestBody(Employee employee) throws JsonProcessingException {
        return objectMapper.writeValueAsString(employee);
    }

    private String parseJSON(String resource) throws Exception {
        try (InputStream stream = this.getClass().getResourceAsStream(resource)) {
            return IOUtils.toString(stream, Charset.defaultCharset());
        }
    }
}
