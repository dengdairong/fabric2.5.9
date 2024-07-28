package com.fabric.fabric.config;

import com.fabric.fabric.dto.ResponseDto;
import org.hyperledger.fabric.gateway.ContractException;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Throwable.class)
    public ResponseDto<String> handleAllExceptions(Exception ex) {
        // 记录日志，处理其他逻辑
        if (ex instanceof ContractException) {
            ContractException e = (ContractException) ex;
            if (e.getProposalResponses().size() > 0) {
                ProposalResponse[] pr = e.getProposalResponses().toArray(new ProposalResponse[0]);
                return ResponseDto.fail(pr[0].getMessage());
            }
            return ResponseDto.fail("系统异常: " + ex.getMessage());
        } else {
            return ResponseDto.fail("系统异常: " + ex.getMessage());
        }
    }
}