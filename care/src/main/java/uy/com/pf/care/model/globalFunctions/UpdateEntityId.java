package uy.com.pf.care.model.globalFunctions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uy.com.pf.care.infra.config.ParamConfig;

//Actualiza el entityId de un User con el id de la entidad recién persistida.
//Si es un paciente, entityId es el id del paciente. Si es un cuidador formal, entityId es su id.
//Igual para cada rol.

@Component //Componente de spring para poder ser inyectado con @Autowired
public class UpdateEntityId {
    @Autowired
    private ParamConfig paramConfig;

    public ResponseEntity<Boolean> execute(String userId, int ordinalRol, String entityId) {

        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                getStartUrl() + "users/updateEntityId/" + userId + "/" + ordinalRol + "/"  + entityId,
                HttpMethod.PUT,
                null,
                Boolean.class
        );
    }

    private String getStartUrl() {
        if (paramConfig == null)
            throw new IllegalStateException("ParamConfig no está inicializado");

        return paramConfig.getProtocol() + "://" + paramConfig.getSocket() + "/";
    }
}


