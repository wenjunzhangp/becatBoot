package com.baozi.service.impl.coupon;

import com.baozi.dao.coupon.LpzTaobaoCouponMapper;
import com.baozi.entity.coupon.LpzTaobaoCoupon;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.baozi.service.coupon.ILpzTaobaoCouponService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wenjunzhangp
 * @since 2019-05-14
 */
@Service
public class LpzTaobaoCouponServiceImpl extends ServiceImpl<LpzTaobaoCouponMapper, LpzTaobaoCoupon> implements ILpzTaobaoCouponService {

    @Autowired(required = false)
    LpzTaobaoCouponMapper lpzTaobaoCouponMapper;

    /**
     * 优惠券列表分页查询
     *
     * @param paramMap
     * @return
     */
    @Override
    public PageInfo<LpzTaobaoCoupon> selectCouponList(Map<String, Object> paramMap) {
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()),Integer.valueOf(paramMap.get("limit").toString()),true);
        List<LpzTaobaoCoupon> dataList = lpzTaobaoCouponMapper.selectCouponList(paramMap);
        return new PageInfo<LpzTaobaoCoupon>(dataList);
    }

    /**
     * 查询所有优惠券数据灌入es中
     *
     * @return
     */
    @Override
    public PageInfo<LpzTaobaoCoupon> selectLpzTaobaoCouponAll(Map<String,Object> paramMap) {
        PageHelper.startPage(Integer.valueOf(paramMap.get("page").toString()),Integer.valueOf(paramMap.get("limit").toString()),true);
        List<LpzTaobaoCoupon> dataList = lpzTaobaoCouponMapper.selectLpzTaobaoCouponAll(paramMap);
        return new PageInfo<LpzTaobaoCoupon>(dataList);
    }
}
