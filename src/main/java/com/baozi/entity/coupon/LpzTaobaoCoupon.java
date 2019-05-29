package com.baozi.entity.coupon;

import com.baomidou.mybatisplus.activerecord.Model;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 
 * </p>
 *
 * @author wenjunzhangp
 * @since 2019-05-14
 */
@TableName("lpz_taobao_coupon")
@Data
public class LpzTaobaoCoupon extends Model<LpzTaobaoCoupon> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private String id;
    /**
     * 商品名称
     */
	private String goodsname;
    /**
     * 商品卖点
     */
	private String goodsremark;
    /**
     * 在售价
     */
	private BigDecimal onlineprice;
    /**
     * 折后价
     */
	private BigDecimal couponprice;
    /**
     * 累计销量
     */
	private Integer sellcount;
    /**
     * 上新时间
     */
	private String update;
    /**
     * 卷入库更新时间
     */
	private Date createdate;
    /**
     * 领劵地址
     */
	private String linkurl;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
