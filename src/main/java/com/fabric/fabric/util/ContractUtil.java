package com.fabric.fabric.util;

import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ContractUtil implements CommandLineRunner {
    @Autowired
    private Gateway gateway;
    @Value("${fabric.chaincodeId}")
    private String chaincodeId;
    @Value("${fabric.channel}")
    private String channel;
    @Value("${fabric.contractNames}")
    private List<String> contractNames;
    private Map<String, Contract> map = new ConcurrentHashMap<>();

    public Contract getContract(String name) {
        Contract contract = map.get(name);
        if (contract == null) {
            // 获取mychannel的网络 也就是通道
            Network network = gateway.getNetwork(channel);
            // 获取contract的链码 名字为fabcar
            contract = network.getContract(chaincodeId, name);
            map.put(name, contract);
        }
        return contract;
    }

    @Override
    public void run(String... args) throws Exception {
        if (contractNames != null && contractNames.size() > 0) {
            Network network = gateway.getNetwork(channel);
            for (String contractName : contractNames) {
                map.put(contractName,  network.getContract(chaincodeId, contractName));
            }
        }
    }
}
