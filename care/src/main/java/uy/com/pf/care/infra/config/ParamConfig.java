package uy.com.pf.care.infra.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
public class ParamConfig {

    /*
    Datos de prueba
     */
//    @Value("${cadena1}")
//    private String cadena;
//
//    @Value("${logica1}")
//    private Boolean logica;
//
//    @Value("${varNum}")
//    private Integer entero;
//
//    @Value("${lista}")
//    private List<Integer> lista;

    /*
    Datos reales
     */
    @Value("${protocol}")
    private String protocol;

    @Value("${socket}")
    private String socket;

}
