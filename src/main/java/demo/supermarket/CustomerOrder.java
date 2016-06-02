package demo.supermarket;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 订单
 */
public class CustomerOrder {

    /**
     * 订单id生成器
     */
    public static final AtomicInteger orderIdGenerator = new AtomicInteger(1);

    /**
     * 订单状态定义
     */
    public static final int ORDER_CREATED = 1;
    public static final int ORDER_FINISHED = 2;

    /**
     * 订单id
     */
    private int orderId;

    /**
     * 关联商品
     */
    private Goods goods;

    /**
     * 订单状态
     */
    private volatile int status = CustomerOrder.ORDER_CREATED;

    /**
     * 订单创建时间
     */
    private long created;

    /**
     * 订单完成时间
     */
    private long finished;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getFinished() {
        return finished;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }
}
