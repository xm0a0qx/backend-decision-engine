package org.example.service;

import org.example.dto.DecisionDto;
import org.example.dto.request.DecisionRequest;
import org.example.dto.response.DecisionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DecisionEngineServiceTest {

    @Mock
    private DecisionEngineService service;
    private DecisionDto customerWithDebtDto;
    private DecisionDto customerWithMaxAmountDto;
    private DecisionDto customerWithMaxAmountAndIncreasedPeriodDto;
    private DecisionResponse responseNegative;
    private DecisionResponse responsePositiveWithMaxAmount;
    private DecisionResponse responsePositiveWithMaxAmountAndIncreasedPeriod;

    @BeforeEach
    void setUp() {
        responseNegative = new DecisionResponse(false, BigDecimal.ZERO, 0);
        responsePositiveWithMaxAmount = new DecisionResponse(true, BigDecimal.valueOf(10000), 12);
        responsePositiveWithMaxAmountAndIncreasedPeriod = new DecisionResponse(true, BigDecimal.valueOf(10000), 60);

        customerWithDebtDto = new DecisionDto("37913110284", BigDecimal.valueOf(2000), 12);
        customerWithMaxAmountDto = new DecisionDto("37913118284", BigDecimal.valueOf(2000), 12);
        customerWithMaxAmountAndIncreasedPeriodDto = new DecisionDto("37913114284", BigDecimal.valueOf(6000), 12);
    }

    @Test
    void shouldMakeNegativeDecisionWhenCustomerHasDebt() {
        when(service.makeDecision(customerWithDebtDto)).thenReturn(responseNegative);
        DecisionResponse decisionResponse = service.makeDecision(customerWithDebtDto);
        verify(service, times(1)).makeDecision(customerWithDebtDto);
        Assertions.assertFalse(decisionResponse.getIsAmountApproved());
        Assertions.assertEquals(BigDecimal.ZERO, decisionResponse.getAmount());
        Assertions.assertEquals(0, decisionResponse.getPeriod());
    }

    @Test
    void shouldMakePositiveDecisionWithMaxAmount() {
        when(service.makeDecision(customerWithMaxAmountDto)).thenReturn(responsePositiveWithMaxAmount);
        DecisionResponse decisionResponse = service.makeDecision(customerWithMaxAmountDto);
        verify(service, times(1)).makeDecision(customerWithMaxAmountDto);
        Assertions.assertTrue(decisionResponse.getIsAmountApproved());
        Assertions.assertEquals(BigDecimal.valueOf(10000), decisionResponse.getAmount());
        Assertions.assertEquals(12, decisionResponse.getPeriod());
    }

    @Test
    void shouldMakePositiveDecisionWithMaxAmountAndIncreasedPeriod() {
        when(service.makeDecision(customerWithMaxAmountAndIncreasedPeriodDto)).thenReturn(responsePositiveWithMaxAmountAndIncreasedPeriod);
        DecisionResponse decisionResponse = service.makeDecision(customerWithMaxAmountAndIncreasedPeriodDto);
        verify(service, times(1)).makeDecision(customerWithMaxAmountAndIncreasedPeriodDto);
        Assertions.assertTrue(decisionResponse.getIsAmountApproved());
        Assertions.assertEquals(BigDecimal.valueOf(10000), decisionResponse.getAmount());
        Assertions.assertEquals(60, decisionResponse.getPeriod());
    }
}