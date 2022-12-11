package com.liyu.coupon.customer.feign;

import com.liyu.coupon.customer.feign.fallback.TemplateServiceFallbackFactory;
import com.liyu.coupon.template.api.beans.CouponTemplateInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Map;

@FeignClient(contextId = "templateService",value = "coupon-template-serv", path = "/template",
//        fallback = TemplateServiceFallback.class,
        fallbackFactory = TemplateServiceFallbackFactory.class)
public interface TemplateService {

    // 读取优惠券
    @GetMapping("/getTemplate")
    CouponTemplateInfo getTemplate(@RequestParam("id") Long id);

    // 批量获取
    @GetMapping("/getBatch")
    Map<Long, CouponTemplateInfo> getTemplateInBatch(@RequestParam("ids") Collection<Long> ids);

    // 优惠券无效化
    @DeleteMapping("/deleteTemplate")
    void deleteTemplate(@RequestParam("id") Long id);
}
