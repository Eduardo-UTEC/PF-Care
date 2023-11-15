package uy.com.pf.care.model.globalFunctions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.lang.Math.*;

//@Component //Componente de spring para poder ser inyectado con @Autowired
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonthlyRequestStats {
    private int month;
    private float totalRequests = 0;
    private int totalRequestsPercentage = 0; //Se carga a nivel superior
    private float matchedRequests = 0;
    private float unmatchedRequests = 0;
    private float pendingRequests = 0;
    private int matchRequestsPercentage = 0;
    private int unmatchRequestsPercentage = 0;
    private int pendingRequestsPercentage = 0;

    public void incrementTotalRequests() {
        totalRequests++;
    }
    public void incrementMatchedRequests() {
        matchedRequests++;
    }
    public void incrementUnmatchedRequests() {
        unmatchedRequests++;
    }
    public void incrementPendingRequests() {
        pendingRequests++;
    }

    public void calculatePercentages() {
        if (totalRequests > 0) {
            matchRequestsPercentage = round(matchedRequests / totalRequests * 100);
            unmatchRequestsPercentage = round(unmatchedRequests / totalRequests * 100);
            pendingRequestsPercentage = round(pendingRequests / totalRequests * 100);
        }
    }
}
