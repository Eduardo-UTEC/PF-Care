package uy.com.pf.care.infra.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class ParamConfig {

    @Value("${cadena1}")
    private String cadena;

    @Value("${logica1}")
    private Boolean logica;

    @Value("${varNum}")
    private Integer entero;

    @Value("${lista}")
    private List<Integer> lista;

}
