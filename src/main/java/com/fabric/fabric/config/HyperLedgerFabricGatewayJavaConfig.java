package com.fabric.fabric.config;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Identities;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Properties;

@Configuration
@Slf4j
public class HyperLedgerFabricGatewayJavaConfig {
    @Value("${fabric.path}")
    private String path;

    @Bean
    public Properties properties() throws Exception {
        if (!StringUtils.hasText(path)) {
            path = HyperLedgerFabricGatewayJavaConfig.class.getResource("/").getPath().substring(1);
        }
        // 主要读取java的配置文件
        Properties properties = new Properties();
        InputStream inputStream = Gateway.class.getResourceAsStream("/fabric.config.properties");
        properties.load(inputStream);
        return properties;
    }

    @Bean(destroyMethod = "close")
    public Gateway gateway(Properties properties) throws Exception {
        String networkConfigPath = path + properties.getProperty("networkConfigPath");
        String certificatePath = path + properties.getProperty("certificatePath");
        X509Certificate certificate = readX509Certificate(Paths.get(certificatePath));
        String privateKeyPath = path + properties.getProperty("privateKeyPath");
        PrivateKey privateKey = getPrivateKey(Paths.get(privateKeyPath));
        Wallet wallet = Wallets.newInMemoryWallet();
        wallet.put("user1", Identities.newX509Identity("Org2MSP", certificate, privateKey));
        Gateway.Builder builder = Gateway.createBuilder()
                .identity(wallet, "user1")
                .networkConfig(Paths.get(networkConfigPath));

        Gateway gateway = builder.connect();

        log.info("=========================================== connected fabric gateway {} ", gateway);

        return gateway;
    }

    /*获取证书的办法*/
    private static X509Certificate readX509Certificate(final Path certificatePath) throws IOException, CertificateException {
        // 通过路径获取到文件 如果成功获取 返回身份识别证书的结果
        try (Reader certificateReader = Files.newBufferedReader(certificatePath, StandardCharsets.UTF_8)) {
            return Identities.readX509Certificate(certificateReader);
        }
    }

    /*获取私钥的办法 */
    private static PrivateKey getPrivateKey(final Path privateKeyPath) throws IOException, InvalidKeyException {
        // 通过路径获取到文件 如果成功获取 返回身份识别私钥的结果
        try (Reader privateKeyReader = Files.newBufferedReader(privateKeyPath, StandardCharsets.UTF_8)) {
            return Identities.readPrivateKey(privateKeyReader);
        }
    }
}