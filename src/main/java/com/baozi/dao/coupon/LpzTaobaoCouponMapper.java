package com.baozi.dao.coupon;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baozi.entity.coupon.LpzTaobaoCoupon;

import java.util.List;
import java.util.Map;

/**
 * <p>
  *  Mapper 接口
 * </p>
 *
 * @author wenjunzhangp
 * @since 2019-05-14
 */
public interface LpzTaobaoCouponMapper extends BaseMapper<LpzTaobaoCoupon> {

    List<LpzTaobaoCoupon> selectCouponList(Map<String,Object> paramMap);

    List<LpzTaobaoCoupon> selectLpzTaobaoCouponAll(Map<String,Object> paramMap);
}