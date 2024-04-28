package org.example.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class DecisionRequest {
    @NonNull
    private String personalCode;
    @DecimalMin(value = "2000.0")
    @DecimalMax(value = "10000.0")
    @NonNull
    private BigDecimal loanAmount;
    @NonNull
    @Min(value = 12, message = "Period have to be more or equal then 12 months")
    @Max(value = 60, message = "Period have to be less then or equal 60")
    private Integer periodInMonths;
}
