package org.example.controller;

import jakarta.validation.Valid;
import org.apache.logging.log4j.Logger;
import org.example.dto.DecisionDto;
import org.example.dto.request.DecisionRequest;
import org.example.dto.response.DecisionResponse;
import org.example.mapper.MapperUtil;
import org.example.service.DecisionEngineService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.logging.log4j.LogManager.getLogger;

@RestController
@RequestMapping("/v1/engine")
public class DecisionEngineController {
    private static final Logger logger = getLogger(DecisionEngineController.class);

    private final DecisionEngineService decisionEngineService;
    private final MapperUtil mapperUtil;

    public DecisionEngineController(DecisionEngineService decisionEngineService, MapperUtil mapperUtil) {
        this.decisionEngineService = decisionEngineService;
        this.mapperUtil = mapperUtil;
    }

    @PostMapping("/decide")
    public ResponseEntity<DecisionResponse> getDecision(@RequestBody @Valid DecisionRequest request, BindingResult errors) {
        if (errors.hasErrors()) {
            logger.error("Validation error occurred while making decision - {}", errors.getAllErrors());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        DecisionDto decisionDto = mapperUtil.mapRequestToDto(request);
        return new ResponseEntity<>(decisionEngineService.makeDecision(decisionDto), HttpStatus.OK);
    }
}
