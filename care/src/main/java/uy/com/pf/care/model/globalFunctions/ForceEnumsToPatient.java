package uy.com.pf.care.model.globalFunctions;

import uy.com.pf.care.model.documents.Patient;
import uy.com.pf.care.model.enums.PersonGenderEnum;
import uy.com.pf.care.model.enums.RelationshipEnum;

public class ForceEnumsToPatient {
    public static void execute(Patient patient){

        //PersonGenderEnum
        switch (patient.getGender()){
            case MALE -> patient.setGender(PersonGenderEnum.MALE);
            case FEMALE -> patient.setGender(PersonGenderEnum.FEMALE);
            case TRANS -> patient.setGender(PersonGenderEnum.TRANS);
            case TRANSM -> patient.setGender(PersonGenderEnum.TRANSM);
            case TRANSF -> patient.setGender(PersonGenderEnum.TRANSF);
        }

        //informalCaregiverObject.RelationshipEnum
        patient.getInformalCaregivers().forEach(informalCaregiverObject -> {
            switch (informalCaregiverObject.getRelationship()){
                case NOBODY -> informalCaregiverObject.setRelationship(RelationshipEnum.NOBODY);
                case FATHER -> informalCaregiverObject.setRelationship(RelationshipEnum.FATHER);
                case MOTHER -> informalCaregiverObject.setRelationship(RelationshipEnum.MOTHER);
                case BROTHER -> informalCaregiverObject.setRelationship(RelationshipEnum.BROTHER);
                case SISTER -> informalCaregiverObject.setRelationship(RelationshipEnum.SISTER);
                case SON -> informalCaregiverObject.setRelationship(RelationshipEnum.SON);
                case DAUGHTER -> informalCaregiverObject.setRelationship(RelationshipEnum.DAUGHTER);
                case NEPHEW -> informalCaregiverObject.setRelationship(RelationshipEnum.NEPHEW);
                case NIECE -> informalCaregiverObject.setRelationship(RelationshipEnum.NIECE);
                case GRANDSON -> informalCaregiverObject.setRelationship(RelationshipEnum.GRANDSON);
                case GRANDDAUGHTER -> informalCaregiverObject.setRelationship(RelationshipEnum.GRANDDAUGHTER);
                case UNCLE -> informalCaregiverObject.setRelationship(RelationshipEnum.UNCLE);
                case AUNT -> informalCaregiverObject.setRelationship(RelationshipEnum.AUNT);
                case FRIEND -> informalCaregiverObject.setRelationship(RelationshipEnum.FRIEND);
                case NEIGHBOR -> informalCaregiverObject.setRelationship(RelationshipEnum.NEIGHBOR);
                case COWORKER -> informalCaregiverObject.setRelationship(RelationshipEnum.COWORKER);
            }
        });

        //ReferenceCaregiver.RelatioshipEnum
        /*switch (patient.getReferenceCaregiver().getRelationship()){
            case NOBODY -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.NOBODY);
            case FATHER -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.FATHER);
            case MOTHER -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.MOTHER);
            case BROTHER -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.BROTHER);
            case SISTER -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.SISTER);
            case SON -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.SON);
            case DAUGHTER -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.DAUGHTER);
            case NEPHEW -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.NEPHEW);
            case NIECE -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.NIECE);
            case GRANDSON -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.GRANDSON);
            case GRANDDAUGHTER -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.GRANDDAUGHTER);
            case UNCLE -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.UNCLE);
            case AUNT -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.AUNT);
            case FRIEND -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.FRIEND);
            case NEIGHBOR -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.NEIGHBOR);
            case COWORKER -> patient.getReferenceCaregiver().setRelationship(RelationshipEnum.COWORKER);
        }


        patient.getReferenceCaregiver().getDayTimeRange().forEach(dayTimeRangeObject -> {
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
         */


    }
}
