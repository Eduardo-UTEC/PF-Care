package uy.com.pf.care.model.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uy.com.pf.care.model.enums.PersonGenderEnum;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonObject {
    private String name1;
    private String name2;
    private String surname1;
    private String surname2;
    private PersonGenderEnum gender;
    private AddressObject address;
    private String  telephone;
    private String mail;
    private Integer identificationDocument;
    private LocalDate dateBirth;
}
