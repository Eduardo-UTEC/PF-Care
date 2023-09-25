package uy.com.pf.care.model.documents;

import jakarta.validation.Valid;
import jdk.jfr.BooleanFlag;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.enums.RequestStatusEnum;
import uy.com.pf.care.model.objects.UserDonationRequestObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document("DonationRequest")
//@CompoundIndex(def = "{'countryName':1, 'departmentName':1, 'name':1}", unique = true)
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class DonationRequest {

    @Id
    private String donationRequestId;

    private UserDonationRequestObject userDonationRequest; // Usuario que inició la solicitud
    private String volunteerCompanyId; //Empresa que tomó la solicitud.
    private LocalDate requestDate;
    private RequestStatusEnum requestStatus;

    @Valid
    private List<Material> materials = new ArrayList<>();

    @BooleanFlag
    private Boolean active;

}
