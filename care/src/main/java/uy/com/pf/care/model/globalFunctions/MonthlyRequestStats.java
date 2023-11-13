package uy.com.pf.care.model.globalFunctions;

import javafx.css.Match;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

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
    private int matchPercentage = 0;
    private int  unmatchPercentage = 0;
    private int pendingPercentage = 0;

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
            matchPercentage = Math.round(matchedRequests / totalRequests * 100);
            unmatchPercentage = Math.round(unmatchedRequests / totalRequests * 100);
            pendingPercentage = Math.round(pendingRequests / totalRequests * 100);
        }
    }
}
