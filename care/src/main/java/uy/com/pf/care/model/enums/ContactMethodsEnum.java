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
    CALL("Llamada"),
    WHATSAPP("WhatsApp"),
    TELEGRAM("Telegram"),
    SMS("SMS"),
    MAIL("e-mail");

    private String name;
}
