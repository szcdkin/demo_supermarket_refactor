package demo.task;

import demo.supermarket.Cashier;
import demo.supermarket.CustomerOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CashierTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 处理支付的收银台
     */
    private Cashier cashier;

    /**
     * 待支付的订单
     */
    private CustomerOrder order;

    public CashierTask(Cashier cashier, CustomerOrder order) {
        this.cashier = cashier;
        this.order = order;
    }

    public void run() {
        try {
            if (order != null) {
                cashier.payOrder(order);
            }

            if (Thread.interrupted()) {
                throw new InterruptedException("");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (logger.isErrorEnabled()) {
                logger.error("cashier task interrupted error", e);
            }
        }
    }
}
