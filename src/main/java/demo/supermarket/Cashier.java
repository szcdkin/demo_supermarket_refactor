package demo.supermarket;

import demo.supermarket.exception.InventoryShortageException;
import demo.supermarket.exception.OrderHasFinishedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cashier {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 收银台个数
     */
    public static final int CASHIER_NUM = 3;

    /**
     * 当前收银台id
     */
    private int cashierId;

    /**
     * 当前收银台处理订单数
     */
    private int processOrdersNum = 0;

    /**
     * 超市数据存取接口
     */
    private Supermarket supermarket = new Supermarket();

    public Cashier(int cashierId, Supermarket supermarket) {
        this.cashierId = cashierId;
        this.supermarket = supermarket;
    }

    /**
     * 支付订单
     * @param order
     */
    public void payOrder(CustomerOrder order) {
        if (order != null) {
            if (logger.isInfoEnabled()) {
                logger.info("收银台处理订单支付, cashierId={}, orderId={}", this.cashierId, order.getOrderId());
            }

            try {
                supermarket.payOrder(order);
                processOrdersNum++;
            } catch (InventoryShortageException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("收银台处理订单支付失败, 库存不足，cashierId={}, orderId={}", this.cashierId, order.getOrderId());
                }
            } catch (OrderHasFinishedException e) {
                if (logger.isErrorEnabled()) {
                    logger.error("收银台处理订单支付失败, 订单已完成，cashierId={}, orderId={}", this.cashierId, order.getOrderId());
                }
            }
        }
    }

    public int getCashierId() {
        return cashierId;
    }

    public void setCashierId(int cashierId) {
        this.cashierId = cashierId;
    }

    public int getProcessOrdersNum() {
        return processOrdersNum;
    }

    public void setProcessOrdersNum(int processOrdersNum) {
        this.processOrdersNum = processOrdersNum;
    }

    public Supermarket getSupermarket() {
        return supermarket;
    }

    public void setSupermarket(Supermarket supermarket) {
        this.supermarket = supermarket;
    }
}
