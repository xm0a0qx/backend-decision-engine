package org.example.controller;

import org.example.dto.DecisionDto;
import org.example.dto.response.DecisionResponse;
import org.example.mapper.MapperUtil;
import org.example.service.DecisionEngineService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.example.util.Utils.asJsonString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
class DecisionEngineControllerTest {

    @Mock
    private DecisionEngineService service;
    @Mock
    private MapperUtil util;
    @Autowired
    private MockMvc mockMvc;
    @InjectMocks
    private DecisionEngineController controller;

    private DecisionDto customerWithDebtDto;
    private DecisionDto customerWithIncorrectDataDto;


    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        customerWithDebtDto = new DecisionDto("37913110284", BigDecimal.valueOf(2000), 12);
        customerWithIncorrectDataDto = new DecisionDto("", BigDecimal.ZERO, 11);
    }

    @Test
    void shouldMakeDecision() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/engine/decide")
                                .content(asJsonString(customerWithDebtDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

    }

    @Test
    void shouldNotMakeDecision() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/v1/engine/decide")
                                .content(asJsonString(customerWithIncorrectDataDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }
}