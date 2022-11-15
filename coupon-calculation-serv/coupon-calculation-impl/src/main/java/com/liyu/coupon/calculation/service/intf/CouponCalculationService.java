package com.liyu.coupon.calculation.service.intf;

import com.liyu.coupon.calculation.api.beans.ShoppingCart;
import com.liyu.coupon.calculation.api.beans.SimulationOrder;
import com.liyu.coupon.calculation.api.beans.SimulationResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface CouponCalculationService {

    ShoppingCart calculateOrderPrice(@RequestBody ShoppingCart cart);

    SimulationResponse simulateOrder(@RequestBody SimulationOrder cart);
}
