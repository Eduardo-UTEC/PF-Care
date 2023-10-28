package uy.com.pf.care.model.globalFunctions;

import jakarta.annotation.Nullable;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import uy.com.pf.care.exceptions.SetMatchException;
import uy.com.pf.care.infra.config.ParamConfig;
import uy.com.pf.care.model.enums.RoleEnum;

//Hace o quita el match entre un paciente y un voluntario

@Component //Componente de spring para poder ser inyectado con @Autowired
@Log
public class SetMatch {
    @Autowired
    private ParamConfig paramConfig;

    public ResponseEntity<Boolean> execute(
            String patientId, String volunteerPersonId, RoleEnum role, @Nullable Boolean isMatch ) {

        try {
            RestTemplate restTemplate = new RestTemplate();

            if (role == RoleEnum.VOLUNTEER_PERSON) {
                return restTemplate.exchange(
                        getStartUrl() +
                                "patients/setMatchVolunteerPerson/" + patientId + "/" + volunteerPersonId + "/" + isMatch,
                        HttpMethod.PUT,
                        null,
                        Boolean.class
                );

                //Paciente
            } else {
                return restTemplate.exchange(
                        getStartUrl() +
                                "volunteer_people/receivePatientRequest/" + volunteerPersonId + "/" + patientId,
                        HttpMethod.PUT,
                        null,
                        Boolean.class
                );
            }

        } catch(Exception e){
            String msg;
            if (role == RoleEnum.VOLUNTEER_PERSON)
                msg = "Error seteando el match del lado del paciente";
            else
                msg = "Error recibiendo solicitud de contacto del paciente, del lado de la persona voluntaria";

            log.info(msg + ": " + e.getMessage());
            throw new SetMatchException(msg);
        }
    }

    private String getStartUrl() {
        if (paramConfig == null)
            throw new IllegalStateException("ParamConfig no está inicializado");

        return paramConfig.getProtocol() + "://" + paramConfig.getSocket() + "/";
    }
}


