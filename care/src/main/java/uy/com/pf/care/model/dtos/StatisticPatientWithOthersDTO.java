package uy.com.pf.care.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatisticPatientWithOthersDTO {
    private int month;
    private int totalRequests;
    private int matchedRequests;
    private int unmatchedRequests;
    private int pendingRequests;
    private int matchPercentage;
    private int unmatchPercentage;
    private int pendingPercentage;
}
