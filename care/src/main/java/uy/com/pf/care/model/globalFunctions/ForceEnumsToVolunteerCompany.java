package uy.com.pf.care.model.globalFunctions;

import uy.com.pf.care.model.documents.VolunteerCompany;
import uy.com.pf.care.model.enums.ContactMethodsEnum;
import uy.com.pf.care.model.enums.DaysWeekEnum;
import uy.com.pf.care.model.enums.PersonGenderEnum;

import java.util.ArrayList;
import java.util.List;

public class ForceEnumsToVolunteerCompany {

    public static void execute(VolunteerCompany volunteerCompany){

        //contactMethods
        List<ContactMethodsEnum> modifiedContactMethods = new ArrayList<>();
        volunteerCompany.getContactMethods().forEach(contactMethodsEnum -> {
            ContactMethodsEnum contact = null;
            switch (contactMethodsEnum){
                case CALL -> contact = ContactMethodsEnum.CALL;
                case WHATSAPP -> contact = ContactMethodsEnum.WHATSAPP;
                case TELEGRAM -> contact = ContactMethodsEnum.TELEGRAM;
                case SMS -> contact = ContactMethodsEnum.SMS;
                case MAIL -> contact = ContactMethodsEnum.MAIL;
            }
            modifiedContactMethods.add(contact);
        });
        volunteerCompany.setContactMethods(modifiedContactMethods);
    }
}
