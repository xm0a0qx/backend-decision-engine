package org.example.dto.response;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@RequiredArgsConstructor
@Setter
@Getter
public class DecisionResponse {
    @NonNull
    private Boolean isAmountApproved;
    @NonNull
    private BigDecimal amount;
    @NonNull
    private Integer period;
}
