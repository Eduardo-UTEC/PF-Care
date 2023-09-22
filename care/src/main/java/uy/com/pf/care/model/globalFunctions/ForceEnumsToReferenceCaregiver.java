package uy.com.pf.care.model.globalFunctions;

import uy.com.pf.care.model.documents.ReferenceCaregiver;
import uy.com.pf.care.model.enums.DaysWeekEnum;
import uy.com.pf.care.model.enums.RelationshipEnum;

public class ForceEnumsToReferenceCaregiver {

    public static void execute(ReferenceCaregiver referenceCaregiver){

        //RelatioshipEnum
        referenceCaregiver.getPatients().forEach(patientLinkedReferentObject -> {
            switch (patientLinkedReferentObject.getRelationship()) {
                case NOBODY -> patientLinkedReferentObject.setRelationship(RelationshipEnum.NOBODY);
                case FATHER -> patientLinkedReferentObject.setRelationship(RelationshipEnum.FATHER);
                case MOTHER -> patientLinkedReferentObject.setRelationship(RelationshipEnum.MOTHER);
                case BROTHER -> patientLinkedReferentObject.setRelationship(RelationshipEnum.BROTHER);
                case SISTER -> patientLinkedReferentObject.setRelationship(RelationshipEnum.SISTER);
                case SON -> patientLinkedReferentObject.setRelationship(RelationshipEnum.SON);
                case DAUGHTER -> patientLinkedReferentObject.setRelationship(RelationshipEnum.DAUGHTER);
                case NEPHEW -> patientLinkedReferentObject.setRelationship(RelationshipEnum.NEPHEW);
                case NIECE -> patientLinkedReferentObject.setRelationship(RelationshipEnum.NIECE);
                case GRANDSON -> patientLinkedReferentObject.setRelationship(RelationshipEnum.GRANDSON);
                case GRANDDAUGHTER -> patientLinkedReferentObject.setRelationship(RelationshipEnum.GRANDDAUGHTER);
                case UNCLE -> patientLinkedReferentObject.setRelationship(RelationshipEnum.UNCLE);
                case AUNT -> patientLinkedReferentObject.setRelationship(RelationshipEnum.AUNT);
                case FRIEND -> patientLinkedReferentObject.setRelationship(RelationshipEnum.FRIEND);
                case NEIGHBOR -> patientLinkedReferentObject.setRelationship(RelationshipEnum.NEIGHBOR);
                case COWORKER -> patientLinkedReferentObject.setRelationship(RelationshipEnum.COWORKER);
            }

        });

        referenceCaregiver.getDayTimeRange().forEach(dayTimeRangeObject -> {
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

    }
    
}
