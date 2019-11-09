package winter.findGuider.web.api;

import entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.contributionPoint.ContributionPointService;
import services.contributionPoint.ContributionPointServiceImpl;
import services.ordertrip.OrderTripService;
import services.ordertrip.OrderTripServiceImpl;

@RestController
@RequestMapping(path = "/Contribution", produces = "application/json")
@CrossOrigin(origins = "*")
public class ContributionPointApi {
    private ContributionPointService contributionPointService;
    private OrderTripService orderTripService;

    @Autowired
    public ContributionPointApi(ContributionPointService contributionPointService, OrderTripService orderTripService) {
        this.contributionPointService = contributionPointService;
        this.orderTripService = orderTripService;
    }

    @PostMapping("/update/{order_id}")
    public void updateContributionPointWithOrderId(@PathVariable("order_id") long order_id) {
        Order order = orderTripService.findOrder(order_id);
//        long point = contributionPointService.calculatePointAfterEachOrder(order);

    }

}
