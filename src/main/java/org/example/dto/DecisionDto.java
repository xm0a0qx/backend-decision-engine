package org.example.dto;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class DecisionDto {
    @NonNull
    private String personalCode;
    @NonNull
    private BigDecimal loanAmount;
    @NonNull
    private Integer periodInMonths;
}
