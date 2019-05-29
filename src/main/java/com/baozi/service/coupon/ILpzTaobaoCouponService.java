package com.baozi.service.coupon;

import com.baomidou.mybatisplus.service.IService;
import com.baozi.entity.coupon.LpzTaobaoCoupon;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wenjunzhangp
 * @since 2019-05-14
 */
public interface ILpzTaobaoCouponService extends IService<LpzTaobaoCoupon> {

    /**
     * 优惠券列表分页查询
     * @param paramMap
     * @return
     */
    PageInfo<LpzTaobaoCoupon> selectCouponList(Map<String,Object> paramMap);

    /**
     * 查询所有优惠券数据灌入es中
     * @return
     */
    PageInfo<LpzTaobaoCoupon> selectLpzTaobaoCouponAll(Map<String,Object> paramMap);
}
