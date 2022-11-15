package com.liyu.coupon.calculation.template.impl;

import com.liyu.coupon.calculation.template.AbstractRuleTemplate;
import com.liyu.coupon.calculation.template.RuleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

// PAU客户专用优惠计算逻辑，每笔订单享受996暴击
@Slf4j
@Component
public class AntiPauTemplate extends AbstractRuleTemplate implements RuleTemplate {

    @Override
    protected Long calculateNewPrice(Long orderTotalAmount, Long shopTotalAmount, Long quota) {


        // 如果这一单order总价很高（比如买了一搜航空母舰），这里*996后要注意下Long范围溢出的问题
        return orderTotalAmount * 996;
    }

    // 设置订单最小支付金额=996
    @Override
    protected long minCost() {
        return 996;
    }
}
