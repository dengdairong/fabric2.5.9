package com.fabric.fabric.controllers;

import com.alibaba.fastjson2.JSONObject;
import com.fabric.fabric.dto.CreateParams;
import com.fabric.fabric.dto.ModifyReportDto;
import com.fabric.fabric.dto.ProduceDrugDto;
import com.fabric.fabric.dto.ResponseDto;
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
@RequestMapping("/contract/manufacturer")
@Slf4j
@AllArgsConstructor
public class ManufacturerContractController {
    public static final String CONTRACT = "ManufacturerContract";
    final ContractUtil contractUtil;

    @PostMapping("/createManufacturer")
    public ResponseDto<Void> createManufacturer(@RequestBody CreateParams params) throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        contract.submitTransaction("CreateManufacturer", params.getName(), params.getContact());
        return ResponseDto.success();
    }

    @PostMapping("/getManufacturers")
    public ResponseDto<String> getManufacturers() throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        byte[] bytes = contract.evaluateTransaction("GetManufacturers");
        return ResponseDto.success(new String(bytes, StandardCharsets.UTF_8));
    }

    @PostMapping("/getManufacturerInfo")
    public ResponseDto<String> getManufacturerInfo(@RequestBody CreateParams params) throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        byte[] bytes = contract.evaluateTransaction("GetManufacturerInfo", params.getName());
        return ResponseDto.success(new String(bytes, StandardCharsets.UTF_8));
    }

    @PostMapping("/produceDrug")
    public ResponseDto<String> produceDrug(@RequestBody ProduceDrugDto params) throws Throwable {
        Contract contract = contractUtil.getContract(CONTRACT);
        byte[] bytes = contract.submitTransaction("ProduceDrug", params.getManufacturerName(), params.getDrugName()
                , params.getPrice());
        return ResponseDto.success(new String(bytes, StandardCharsets.UTF_8));
    }
}