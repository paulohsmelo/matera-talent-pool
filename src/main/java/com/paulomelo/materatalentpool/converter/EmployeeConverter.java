package com.paulomelo.materatalentpool.converter;

import com.paulomelo.materatalentpool.dto.EmployeeRequestDTO;
import com.paulomelo.materatalentpool.model.Employee;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class EmployeeConverter {

    private final ModelMapper modelMapper;

    public EmployeeConverter(final ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Employee convert(final EmployeeRequestDTO dto) {
        return modelMapper.map(dto, Employee.class);
    }
}
