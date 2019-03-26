package com.paulomelo.materatalentpool.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "MIDDLE_INITIAL")
    private String middleInitial;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "DATE_OF_BIRTH")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @Column(name = "DATE_OF_EMPLOYMENT")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfEmployment;

    @Enumerated(EnumType.STRING)
    private EmployeeStatus status = EmployeeStatus.ACTIVE;

    public void inactive() {
        this.status = EmployeeStatus.INACTIVE;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Employee setFirstName(final String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public Employee setMiddleInitial(final String middleInitial) {
        this.middleInitial = middleInitial;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public Employee setLastName(final String lastName) {
        this.lastName = lastName;
        return this;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Employee setDateOfBirth(final LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    public LocalDate getDateOfEmployment() {
        return dateOfEmployment;
    }

    public Employee setDateOfEmployment(final LocalDate dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
        return this;
    }

    public EmployeeStatus getStatus() {
        return status;
    }

}
