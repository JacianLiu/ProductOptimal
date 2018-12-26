package entity;

import com.pinyougou.pojo.TbOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ricky
 * @date create in 2018/12/25
 * TODO 购物车组合类
 *      sellerId 商家ID
 *      sellerName 店铺名称
 *      orderItemList 购物车改店铺商品信息
 */
public class Cart implements Serializable {
    private String sellerId;
    private String sellerName;
    private List<TbOrderItem>orderItemList;


    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public List<TbOrderItem> getOrderItemList() {
        return orderItemList;
    }

    public void setOrderItemList(List<TbOrderItem> orderItemList) {
        this.orderItemList = orderItemList;
    }
}
