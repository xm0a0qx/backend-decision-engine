package org.example.service;

import com.github.vladislavgoltjajev.personalcode.locale.estonia.EstonianPersonalCodeValidator;
import org.apache.logging.log4j.Logger;
import org.example.dto.DecisionDto;
import org.example.dto.response.DecisionResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.apache.logging.log4j.LogManager.getLogger;

@Service
public class DecisionEngineService {
    private static final Logger logger = getLogger(DecisionEngineService.class);
    private final EstonianPersonalCodeValidator validator = new EstonianPersonalCodeValidator();

    public static final int DEBT = 0;
    public static final int SEGMENT_1_CREDIT_MODIFIER = 100;
    public static final int SEGMENT_2_CREDIT_MODIFIER = 300;
    public static final int SEGMENT_3_CREDIT_MODIFIER = 1000;
    public static final BigDecimal MINIMUM_LOAN_AMOUNT = new BigDecimal("2000");
    public static final BigDecimal MAXIMUM_LOAN_AMOUNT = new BigDecimal("10000");
    public static final int MAXIMUM_LOAN_PERIOD = 60;
    public static final BigDecimal LOAN_AMOUNT_STEP = new BigDecimal("100");
    public static final BigDecimal CREDIT_SCORE_POSITIVE_THRESHOLD = BigDecimal.ONE;

    public DecisionResponse makeDecision(DecisionDto dto) {
        int creditModifier = getCreditModifier(dto.getPersonalCode());
        BigDecimal creditScore = getCreditScore(creditModifier, dto.getLoanAmount(), dto.getPeriodInMonths());
        BigDecimal newLoanAmount = dto.getLoanAmount();
        int newLoanPeriod = dto.getPeriodInMonths();

        //validatePersonalCode(dto);

        if (creditScore.compareTo(BigDecimal.ZERO) == 0) {
            logger.info("Loan is not approved for customer {}. Reason: DEBT", dto.getPersonalCode());
            return new DecisionResponse(false, BigDecimal.ZERO, 0);
        }

        if (creditScore.compareTo(CREDIT_SCORE_POSITIVE_THRESHOLD) >= 0) {
            logger.info("Loan approved for customer {} approved with maximum amount {}", dto.getPersonalCode(), MAXIMUM_LOAN_AMOUNT);
            return new DecisionResponse(true, MAXIMUM_LOAN_AMOUNT, dto.getPeriodInMonths());
        }

        while (creditScore.compareTo(CREDIT_SCORE_POSITIVE_THRESHOLD) < 0 && newLoanAmount.compareTo(MINIMUM_LOAN_AMOUNT) >= 0) {
            while (creditScore.compareTo(CREDIT_SCORE_POSITIVE_THRESHOLD) < 0 && newLoanPeriod < MAXIMUM_LOAN_PERIOD) {
                newLoanPeriod++;
                creditScore = getCreditScore(creditModifier, newLoanAmount, newLoanPeriod);
                if (creditScore.compareTo(CREDIT_SCORE_POSITIVE_THRESHOLD) >= 0) {
                    break;
                }
            }

            if (creditScore.compareTo(CREDIT_SCORE_POSITIVE_THRESHOLD) >= 0) {
                break;
            }

            newLoanAmount = newLoanAmount.subtract(LOAN_AMOUNT_STEP);
            creditScore = getCreditScore(creditModifier, newLoanAmount, dto.getPeriodInMonths());
            newLoanPeriod = dto.getPeriodInMonths();
        }

        return new DecisionResponse(true, newLoanAmount, newLoanPeriod);
    }
    /*
    * Validation has been added but disabled for the sake of
    * simplicity to make interaction with different personal codes easier.
    * */
    private void validatePersonalCode(DecisionDto dto) {
        if (!validator.isValid(dto.getPersonalCode())) {
            logger.info("Personal code {} has incorrect format", dto.getPersonalCode());
            throw new IllegalStateException("Invalid personal ID code!");
        }
    }

    private BigDecimal getCreditScore(int creditModifier, BigDecimal amount, int period) {
        return BigDecimal.valueOf(creditModifier).divide(amount, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(period));
    }

    private Integer getCreditModifier(String personalCode) {
        int segment = Integer.parseInt(personalCode.substring(personalCode.length() - 4));

        if (segment < 2500) {
            return DEBT;
        } else if (segment < 5000) {
            return SEGMENT_1_CREDIT_MODIFIER;
        } else if (segment < 7500) {
            return SEGMENT_2_CREDIT_MODIFIER;
        }
        return SEGMENT_3_CREDIT_MODIFIER;
    }
}
