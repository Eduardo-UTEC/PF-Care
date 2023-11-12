package uy.com.pf.care.model.documents;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jdk.jfr.BooleanFlag;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import uy.com.pf.care.model.enums.ContactMethodsEnum;
import uy.com.pf.care.model.objects.AddressObject;
import uy.com.pf.care.model.objects.InterestZonesObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document("VolunteerCompanies")
@CompoundIndexes({
        @CompoundIndex(def = "{'countryName':1, 'name':1, 'telephone':1}", unique = true),
        @CompoundIndex(def = "{'mail':1}", unique = true)
})
@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VolunteerCompany {

    @Id
    private String volunteerCompanyId;

    private String userId;

    @NotNull(message = "La clave 'name' no puede ser nula")
    @NotEmpty(message = "La clave 'name' no puede ser vacía")
    @Size(max = 50, message = "La clave 'name' no puede exceder los 50 caracteres")
    private String name;

    @NotNull(message = "La clave 'address' no puede ser nula")
    private AddressObject address;

    @NotNull(message = "La clave 'telephone' no puede ser nula")
    @NotEmpty(message = "La clave 'telephone' no puede ser vacía")
    @Size(min = 7, max = 30, message = "La clave 'telephone' debe contener entre 7 y 30 caracteres")
    private String  telephone;

    @Size(max = 80, message = "La clave 'mail' no puede exceder los 80 caracteres")
    private String mail;

    @Builder.Default
    private List<String> donationsRequestId = new ArrayList<>(); //Donaciones que aceptó

    @NotNull(message = "La clave interestZones no puede ser nula")
    @Builder.Default
    @Valid
    private List<InterestZonesObject> interestZones = new ArrayList<>();

    @NotNull(message = "La clave contactMethods no puede ser nula")
    private List<ContactMethodsEnum> contactMethods;

    @NotNull(message = "La clave 'photo' no puede ser nula")
    private Byte[] photo;

    //Se toma al persistir.
    private LocalDate registrationDate;

    @BooleanFlag
    private Boolean available;  // Si es False, implica que sus servicios no estan disponibles momentáneamente

    @BooleanFlag
    private Boolean validate;

    @BooleanFlag
    private Boolean deleted;

    @NotNull(message = "El país de residencia de la empresa voluntaria no puede ser nulo")
    @NotEmpty(message = "El país de residencia de la empresa voluntaria no puede ser vacío")
    @Size(max = 30, message = "El pais de residencia de la empresa voluntaria no puede exceder los 30 caracteres")
    private String countryName;

}
