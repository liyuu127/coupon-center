package com.liyu.coupon.customer.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.liyu.coupon.calculation.api.beans.ShoppingCart;
import com.liyu.coupon.calculation.api.beans.SimulationOrder;
import com.liyu.coupon.calculation.api.beans.SimulationResponse;
import com.liyu.coupon.customer.api.beans.RequestCoupon;
import com.liyu.coupon.customer.api.beans.SearchCoupon;
import com.liyu.coupon.customer.dao.entity.Coupon;
import com.liyu.coupon.customer.event.CouponProducer;
import com.liyu.coupon.customer.service.intf.CouponCustomerService;
import com.liyu.coupon.template.api.beans.CouponInfo;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("coupon-customer")
//@AllArgsConstructor
@RequestScope
public class CouponCustomerController {

    private final CouponCustomerService customerService;
    private final CouponProducer couponProducer;

    @Value("${disableCouponRequest:false}")
    private Boolean disableCoupon;

    public CouponCustomerController(CouponCustomerService customerService, CouponProducer couponProducer) {
        this.customerService = customerService;
        this.couponProducer = couponProducer;
    }

    @SentinelResource(value = "requestCoupon")
    @PostMapping("requestCoupon")
    public Coupon requestCoupon(@Valid @RequestBody RequestCoupon request) {
        if (disableCoupon) {
            log.info("暂停领取优惠券");
            return null;
        }
        return customerService.requestCoupon(request);
    }

    // 用户删除优惠券
    @DeleteMapping("deleteCoupon")
    public void deleteCoupon(@RequestParam("userId") Long userId,
                             @RequestParam("couponId") Long couponId) {
        customerService.deleteCoupon(userId, couponId);
    }

    // 用户模拟计算每个优惠券的优惠价格
    @PostMapping("simulateOrder")
    public SimulationResponse simulate(@Valid @RequestBody SimulationOrder order) {
        return customerService.simulateOrderPrice(order);
    }

    // ResponseEntity - 指定返回状态码 - 可以作为一个课后思考题
    @PostMapping("placeOrder")
    public ShoppingCart checkout(@Valid @RequestBody ShoppingCart info) {
        return customerService.placeOrder(info);
    }


    // 实现的时候最好封装一个search object类
    @SentinelResource(value = "customer-findCoupon")
    @PostMapping("findCoupon")
    public List<CouponInfo> findCoupon(@Valid @RequestBody SearchCoupon request) {
        return customerService.findCoupon(request);
    }


    @PostMapping("requestCouponEvent")
    public void requestCouponEvent(@Valid @RequestBody RequestCoupon request) {
        couponProducer.sendCoupon(request);
    }

    // 用户删除优惠券
    @DeleteMapping("deleteCouponEvent")
    public void deleteCouponEvent(@RequestParam("userId") Long userId,
                                  @RequestParam("couponId") Long couponId) {
        couponProducer.deleteCoupon(userId, couponId);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "template")
    @GlobalTransactional(name = "coupon-customer-serv", rollbackFor = Exception.class)
    public void deleteTemplate(@RequestParam("templateId") Long templateId) {
        customerService.deleteCouponTemplate(templateId);
    }

}
