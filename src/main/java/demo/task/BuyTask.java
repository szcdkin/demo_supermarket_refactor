package demo.task;

import demo.supermarket.Buyer;
import demo.supermarket.Cashier;
import demo.supermarket.CashierSelector;
import demo.supermarket.CustomerOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BuyTask implements Runnable {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 执行任务线程池
     */
    private ScheduledExecutorService executorService = null;

    /**
     * 购买商品
     */
    private Buyer buyer;

    /**
     * 收银台选择器
     */
    private CashierSelector cashierSelector;

    /**
     * 收银台列表
     */
    private List<Cashier> cashiers = null;

    public BuyTask(ScheduledExecutorService executorService, Buyer buyer, List<Cashier> cashiers, CashierSelector cashierSelector) {
        this.executorService = executorService;
        this.buyer = buyer;
        this.cashiers = cashiers;
        this.cashierSelector = cashierSelector;
    }

    public void run() {
        try {
            // 生成订单
            CustomerOrder order = buyer.getOrder();

            if (order == null) {
                return;
            }

            // 选择收银台
            Cashier cashier = this.cashierSelector.select(order, cashiers);

            if (logger.isInfoEnabled()) {
                logger.info("处理购买商品生成订单, orderId={}, goodsId={}", order.getOrderId(), order.getGoods().getGoodsId());
            }

            // 支付任务
            CashierTask cashierTask = new CashierTask(cashier, order);

            // 推迟支付时间
            int payDelay = this.getRandom();

            // 提交支付任务
            this.executorService.schedule(cashierTask, payDelay, TimeUnit.SECONDS);

            if (Thread.interrupted()) {
                throw new InterruptedException("");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            if (logger.isErrorEnabled()) {
                logger.error("buy task interrupted error", e);
            }
        }
    }

    private int getRandom() {
        Random random = new Random();
        return random.nextInt(6) + 5;
    }

}
