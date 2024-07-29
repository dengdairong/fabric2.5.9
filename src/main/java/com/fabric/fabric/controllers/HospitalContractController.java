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
@RequestMapping("/contract/hospital")
@Slf4j
@AllArgsConstructor
public class HospitalContractController {
    public static final String CONTRACT = "HospitalContract";
    final ContractUtil contractUtil;

    @PostMapping("/createHospital")
    public ResponseDto<Void> createHospital(@RequestBody CreateParams params) throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        contract.submitTransaction("CreateHospital", params.getName(), params.getContact());
        return ResponseDto.success();
    }

    @PostMapping("/getHospitals")
    public ResponseDto<String> getHospitals() throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        byte[] bytes = contract.evaluateTransaction("GetHospitals");
        return ResponseDto.success(new String(bytes, StandardCharsets.UTF_8));
    }

    @PostMapping("/getPatients")
    public ResponseDto<String> getPatients(@RequestBody HospitalGetPatients params) throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        byte[] bytes = contract.evaluateTransaction("GetPatients", params.getHospitalName());
        return ResponseDto.success(new String(bytes, StandardCharsets.UTF_8));
    }

    @PostMapping("/modifyReport")
    public ResponseDto<String> modifyReport(@RequestBody ModifyReportDto params) throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        byte[] bytes = contract.submitTransaction("ModifyReport", params.getHospitalName(), params.getPatientName()
                , params.getSymptoms(), JSONObject.toJSONString(params.getNeededDrugs()));
        return ResponseDto.success(new String(bytes, StandardCharsets.UTF_8));
    }

    @PostMapping("/buyDrug")
    public ResponseDto<String> buyDrug(@RequestBody BuyDrugDto params) throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        byte[] bytes = contract.submitTransaction("BuyDrug", params.getHospitalName(), params.getManufacturerName()
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