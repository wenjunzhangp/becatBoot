package com.baozi.controller.coupon;

import com.baozi.controller.base.ResponseBase;
import com.baozi.service.coupon.ILpzTaobaoCouponService;
import com.baozi.util.LoggerUtils;
import com.baozi.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wenjunzhangp
 * @since 2019-05-14
 */
@Controller
@RequestMapping("coupon")
@Api(tags = "优惠券controller", description = "优惠券controller")
public class LpzTaobaoCouponController {

    @Autowired
    ILpzTaobaoCouponService lpzTaobaoCouponService;

    /**
     * 优惠券分页列表查询
     * @return
     */
    @RequestMapping("query/coupon/couponList")
    @ResponseBody
    @ApiOperation(value="优惠券分页查询", notes="优惠券分页查询")
    public Map<String,Object> queryCoupon(HttpServletRequest request){
        try {
            Map<String,Object> paramMap = WebUtil.genRequestMapSingle(request);
            return ResponseBase.setResultMapOkByPage(lpzTaobaoCouponService.selectCouponList(paramMap));
        } catch ( Exception e ) {
            LoggerUtils.logError("优惠券分页查询出现异常",e);
            return ResponseBase.setResultMapError(e);
        }
    }
}
