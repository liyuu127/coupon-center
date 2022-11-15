package com.liyu.coupon.calculation.template;

import com.liyu.coupon.calculation.api.beans.ShoppingCart;
import com.liyu.coupon.calculation.template.impl.*;
import com.liyu.coupon.template.api.beans.CouponTemplateInfo;
import com.liyu.coupon.template.api.enums.CouponType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

// 工厂方法根据订单中的优惠券信息，返回对应的Template用于计算优惠价
@Component
@Slf4j
public class CouponTemplateFactory {

    private final MoneyOffTemplate moneyOffTemplate;

    private final DiscountTemplate discountTemplate;

    private final RandomReductionTemplate randomReductionTemplate;

    private final LonelyNightTemplate lonelyNightTemplate;

    private final DummyTemplate dummyTemplate;
    private final AntiPauTemplate antiPauTemplate;

    public CouponTemplateFactory(MoneyOffTemplate moneyOffTemplate, DiscountTemplate discountTemplate, RandomReductionTemplate randomReductionTemplate, LonelyNightTemplate lonelyNightTemplate, DummyTemplate dummyTemplate, AntiPauTemplate antiPauTemplate) {
        this.moneyOffTemplate = moneyOffTemplate;
        this.discountTemplate = discountTemplate;
        this.randomReductionTemplate = randomReductionTemplate;
        this.lonelyNightTemplate = lonelyNightTemplate;
        this.dummyTemplate = dummyTemplate;
        this.antiPauTemplate = antiPauTemplate;
    }

    public RuleTemplate getTemplate(ShoppingCart order) {
        // 不使用优惠券
        if (CollectionUtils.isEmpty(order.getCouponInfos())) {
            // dummy模板直接返回原价，不进行优惠计算
            return dummyTemplate;
        }

        // 获取优惠券的类别
        // 目前每个订单只支持单张优惠券
        CouponTemplateInfo template = order.getCouponInfos().get(0).getTemplate();
        CouponType category = CouponType.convert(template.getType());

        switch (category) {
            // 订单满减券
            case MONEY_OFF:
                return moneyOffTemplate;
            // 随机立减券
            case RANDOM_DISCOUNT:
                return randomReductionTemplate;
            // 午夜下单优惠翻倍
            case LONELY_NIGHT_MONEY_OFF:
                return lonelyNightTemplate;
            // 打折券
            case DISCOUNT:
                return discountTemplate;
            case ANTI_PUA:
                return antiPauTemplate;
            // 未知类型的券模板
            default:
                return dummyTemplate;
        }
    }

}
