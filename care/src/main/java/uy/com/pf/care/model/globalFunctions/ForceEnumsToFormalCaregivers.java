package uy.com.pf.care.model.globalFunctions;

import uy.com.pf.care.model.documents.FormalCaregiver;
import uy.com.pf.care.model.enums.DaysWeekEnum;
import uy.com.pf.care.model.enums.FormalCaregiversEnum;

public class ForceEnumsToFormalCaregivers {
    public static void execute(FormalCaregiver formalCaregiver){

        //FormalCaregiversEnum
        switch (formalCaregiver.getType()){
            case SERVICE_OF_CAREGIVERS -> formalCaregiver.setType(FormalCaregiversEnum.SERVICE_OF_CAREGIVERS);
            case NURSE -> formalCaregiver.setType(FormalCaregiversEnum.NURSE);
            case QUALIFIED_CAREGIVER -> formalCaregiver.setType(FormalCaregiversEnum.QUALIFIED_CAREGIVER);
        }

        //DaysWeekEnum
        formalCaregiver.getDayTimeRange().forEach(dayTimeRangeObject -> {
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
