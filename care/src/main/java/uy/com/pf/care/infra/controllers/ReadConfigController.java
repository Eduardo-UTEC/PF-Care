package uy.com.pf.care.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import uy.com.pf.care.infra.config.ParamConfig;

@RestController
@RequestMapping("/read_config")
public class ReadConfigController {

    @Autowired
    private ParamConfig paramConfig;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> read() {
        try {
            return new ResponseEntity<>(readConfig(), HttpStatus.OK);

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error leyendo archivo de configuracion)");
        }
    }

    private String readConfig() {
        return "[{" +
                "\"cadena\": \"" + paramConfig.getCadena() + "\"," +
                "\"entero\": " + paramConfig.getEntero() + "," +
                "\"logica\": " + paramConfig.getLogica() + "," +
                "\"lista\": " + paramConfig.getLista() +
                "}]";
    }

}