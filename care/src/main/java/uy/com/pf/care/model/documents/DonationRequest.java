package uy.com.pf.care.model.documents;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.enums.RequestStatusEnum;
import uy.com.pf.care.model.objects.UserDonationRequestObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document("DonationRequest")
@CompoundIndex(def = "{'countryName':1, 'departmentName':1, 'requestStatus':1, 'active':1}")
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class DonationRequest {

    @Id
    private String donationRequestId;

    private UserDonationRequestObject userDonationRequest; // Usuario que inició la solicitud
    private String volunteerCompanyId; //Empresa que tomó la solicitud.
    private LocalDate startRequestDate;
    private LocalDate finishRequestDate;
    private String materialId;  //Material solicitado (1 material por solicitud)
    private RequestStatusEnum requestStatus;

    @BooleanFlag
    private Boolean active;

    @NotNull(message = "El departamento en el que se realiza la solicitud no puede ser nulo")
    @NotEmpty(message = "El departamento en el que se realiza la solicitud no puede ser vacío")
    @Size(max = 15, message = "El departamento en el que se realiza la solicitud no puede exceder los 15 caracteres")
    private String departmentName;

    @NotNull(message = "El país en el que se realiza la solicitud no puede ser nulo")
    @NotEmpty(message = "El país en el que se realiza la solicitud no puede ser vacío")
    @Size(max = 15, message = "El pais en el que se realiza la solicitud no puede exceder los 15 caracteres")
    private String countryName;


}
