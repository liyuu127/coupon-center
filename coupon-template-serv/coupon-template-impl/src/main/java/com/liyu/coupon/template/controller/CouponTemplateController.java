package com.liyu.coupon.template.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.liyu.coupon.template.api.beans.CouponTemplateInfo;
import com.liyu.coupon.template.api.beans.PagedCouponTemplateInfo;
import com.liyu.coupon.template.api.beans.TemplateSearchParams;
import com.liyu.coupon.template.dao.CouponTemplateDao;
import com.liyu.coupon.template.service.intf.CouponTemplateService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.sql.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/template")
@AllArgsConstructor
public class CouponTemplateController {

    private final CouponTemplateService couponTemplateService;

    // 创建优惠券
    @PostMapping("/addTemplate")
    public CouponTemplateInfo addTemplate(@Valid @RequestBody CouponTemplateInfo request) {
        log.info("Create coupon template: data={}", request);
        return couponTemplateService.createTemplate(request);
    }

    @PostMapping("/cloneTemplate")
    public CouponTemplateInfo cloneTemplate(@RequestParam("id") Long templateId) {
        log.info("Clone coupon template: data={}", templateId);
        return couponTemplateService.cloneTemplate(templateId);
    }

    // 读取优惠券
    @SentinelResource(value = "getTemplate")
    @GetMapping("/getTemplate")
    public CouponTemplateInfo getTemplate(@RequestParam("id") Long id) {
        log.info("Load template, id={}", id);
        return couponTemplateService.loadTemplateInfo(id);
    }

    // 批量获取
    @GetMapping("/getBatch")
    @SentinelResource(value = "getTemplateInBatch", fallback = "getTemplateInBatch_fallback", blockHandler = "getTemplateInBatch_block")
    public Map<Long, CouponTemplateInfo> getTemplateInBatch(@RequestParam("ids") Collection<Long> ids) {
        log.info("getTemplateInBatch: {}", JSON.toJSONString(ids));
        return couponTemplateService.getTemplateInfoMap(ids);
    }

    public Map getTemplateInBatch_fallback(Collection ids) {
        log.info("接口被降级");
        return Maps.newHashMap();
    }

    public Map getTemplateInBatch_block(Collection ids, BlockException exception) {
        log.info("接口被限流");
        return Maps.newHashMap();
    }

    // 搜索模板
    @PostMapping("/search")
    public PagedCouponTemplateInfo search(@Valid @RequestBody TemplateSearchParams request) {
        log.info("search templates, payload={}", request);
        return couponTemplateService.search(request);
    }

    // 优惠券无效化
    @DeleteMapping("/deleteTemplate")
    public void deleteTemplate(@RequestParam("id") Long id) {
        log.info("Load template, id={}", id);
        couponTemplateService.deleteTemplate(id);
    }
}
