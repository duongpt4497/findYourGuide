package winter.findGuider.web.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import services.Statistic.StatisticService;

import java.util.List;

@RestController
@RequestMapping(path = "/statistic", produces = "application/json")
@CrossOrigin(origins = "*")
public class StatisticController {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService ss) {
        this.statisticService = ss;
    }

    @RequestMapping("/completedTrip")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<String>> getStatisticCompletedTrip() {
        try {
            return new ResponseEntity<>(statisticService.getStatisticCompletedTrip(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("/totalRevenue")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<String>> getStatisticTotalRevenue() {
        try {
            return new ResponseEntity<>(statisticService.getStatisticTotalRevenue(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
