package dev.chijiokeibekwe.librarymanagementsystem.config;

import dev.chijiokeibekwe.librarymanagementsystem.config.properties.RsaKeyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        RsaKeyProperties.class
})
public class BeanConfig {
}
