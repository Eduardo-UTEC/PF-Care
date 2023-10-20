package uy.com.pf.care.infra.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class ParamConfig {

    @Value("${protocol}")
    private String protocol;

    @Value("${socket}")
    private String socket;

}
