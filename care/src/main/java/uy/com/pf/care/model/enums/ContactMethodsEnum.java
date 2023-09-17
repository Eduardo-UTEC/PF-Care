package uy.com.pf.care.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
//@ToString
public enum ContactMethodsEnum {
    CALL(0, "Llamada"),
    WHATSAPP(1, "WhatsApp"),
    TELEGRAM(2, "Telegram"),
    SMS(3, "SMS"),
    MAIL(4, "e-mail");

    private int ordinal;
    private String name;
}
