package com.paulomelo.materatalentpool.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@ApiModel("Represents an employee")
public class EmployeeRequestDTO {

    @ApiModelProperty(value = "First name", required = true, example = "Robert")
    @NotEmpty
    @Size(max = 200)
    private String firstName;

    @ApiModelProperty(value = "Middle name", required = false, example = "Patrick")
    @Size(max = 100)
    private String middleInitial;

    @ApiModelProperty(value = "Last name", required = false, example = "Simon")
    @NotEmpty
    @Size(max = 200)
    private String lastName;

    @ApiModelProperty(value = "Date of birth (format YYYY-MM-DD)", required = true, example = "1985-06-24")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private LocalDate dateOfBirth;

    @ApiModelProperty(value = "Date of employment (format YYYY-MM-DD)", required = false, example = "2016-02-12")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfEmployment;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setMiddleInitial(final String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfEmployment() {
        return dateOfEmployment;
    }

    public void setDateOfEmployment(final LocalDate dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }
}
