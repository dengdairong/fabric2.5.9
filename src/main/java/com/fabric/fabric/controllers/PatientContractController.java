package com.fabric.fabric.controllers;

import com.alibaba.fastjson2.JSONObject;
import com.fabric.fabric.dto.*;
import com.fabric.fabric.util.ContractUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Contract;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/contract/patient")
@Slf4j
@AllArgsConstructor
public class PatientContractController {
    public static final String CONTRACT = "PatientContract";
    final ContractUtil contractUtil;

    @PostMapping("/createPatient")
    public ResponseDto<Void> createPatient(@RequestBody CreatePatientDto params) throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        contract.submitTransaction("CreatePatient", params.getName(), params.getBirthDate(),
                params.getHeight(), params.getWeight(), params.getGender(), params.getContact());
        return ResponseDto.success();
    }

    @PostMapping("/getPatients")
    public ResponseDto<String> getPatients() throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        byte[] bytes = contract.evaluateTransaction("GetPatients");
        return ResponseDto.success(new String(bytes, StandardCharsets.UTF_8));
    }

    @PostMapping("/conductExamination")
    public ResponseDto<String> ConductExamination(@RequestBody ModifyReportDto params) throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        byte[] bytes = contract.submitTransaction("ConductExamination", params.getHospitalName(), params.getPatientName()
                , params.getSymptoms(), JSONObject.toJSONString(params.getNeededDrugs()));
        return ResponseDto.success(new String(bytes, StandardCharsets.UTF_8));
    }

    @PostMapping("/viewReport")
    public ResponseDto<String> viewReport(@RequestBody ViewReportDto params) throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        byte[] bytes = contract.submitTransaction("ViewReport", params.getPatientName(), params.getHospitalName()
                , params.getReportID());
        return ResponseDto.success(new String(bytes, StandardCharsets.UTF_8));
    }

    @PostMapping("/buyDrug")
    public ResponseDto<String> buyDrug(@RequestBody PatientBuyDrugDto params) throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        byte[] bytes = contract.submitTransaction("BuyDrug", params.getPatientName(), params.getHospitalName()
                , params.getDrugName());
        return ResponseDto.success(new String(bytes, StandardCharsets.UTF_8));
    }


    @PostMapping("/traceDrug")
    public ResponseDto<String> traceDrug(@RequestBody TraceDrugDto params) throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        byte[] bytes = contract.submitTransaction("TraceDrug", params.getTraceCode());
        return ResponseDto.success(new String(bytes, StandardCharsets.UTF_8));
    }
}