package org.example.mapper;

import org.example.dto.DecisionDto;
import org.example.dto.request.DecisionRequest;
import org.springframework.stereotype.Service;

@Service
public class MapperUtil {

    public DecisionDto mapRequestToDto(DecisionRequest request) {
        DecisionDto dto = new DecisionDto();
        dto.setPersonalCode(request.getPersonalCode());
        dto.setLoanAmount(request.getLoanAmount());
        dto.setPeriodInMonths(request.getPeriodInMonths());
        return dto;
    }
}
