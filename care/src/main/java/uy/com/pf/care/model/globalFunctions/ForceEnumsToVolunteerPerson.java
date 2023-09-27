package uy.com.pf.care.model.globalFunctions;

import uy.com.pf.care.model.documents.VolunteerPerson;
import uy.com.pf.care.model.enums.ContactMethodsEnum;
import uy.com.pf.care.model.enums.DaysWeekEnum;
import uy.com.pf.care.model.enums.PersonGenderEnum;

import java.util.ArrayList;
import java.util.List;

public class ForceEnumsToVolunteerPerson {

    public static void execute(VolunteerPerson volunteerPerson){

        //gender
        switch (volunteerPerson.getGender()) {
            case MALE -> volunteerPerson.setGender(PersonGenderEnum.MALE);
            case FEMALE -> volunteerPerson.setGender(PersonGenderEnum.FEMALE);
            case TRANS -> volunteerPerson.setGender(PersonGenderEnum.TRANS);
            case TRANSM -> volunteerPerson.setGender(PersonGenderEnum.TRANSM);
            case TRANSF -> volunteerPerson.setGender(PersonGenderEnum.TRANSF);
        }

        //dayTimeRange
        volunteerPerson.getDayTimeRange().forEach(dayTimeRangeObject -> {
            switch (dayTimeRangeObject.getDay()){
                case MON -> dayTimeRangeObject.setDay(DaysWeekEnum.MON);
                case TUE -> dayTimeRangeObject.setDay(DaysWeekEnum.TUE);
                case WED -> dayTimeRangeObject.setDay(DaysWeekEnum.WED);
                case THU -> dayTimeRangeObject.setDay(DaysWeekEnum.THU);
                case FRI -> dayTimeRangeObject.setDay(DaysWeekEnum.FRI);
                case SAT -> dayTimeRangeObject.setDay(DaysWeekEnum.SAT);
                case SUN -> dayTimeRangeObject.setDay(DaysWeekEnum.SUN);
            }
        });

        //contactMethods
        List<ContactMethodsEnum> modifiedContactMethods = new ArrayList<>();
        volunteerPerson.getContactMethods().forEach(contactMethodsEnum -> {
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
        volunteerPerson.setContactMethods(modifiedContactMethods);
    }
}
