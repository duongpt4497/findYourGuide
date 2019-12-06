package services.Statistic;

import java.util.List;

public interface StatisticService {
    List<String> getStatisticCompletedTrip() throws Exception;

    List<String> getStatisticTotalRevenue() throws Exception;
}
