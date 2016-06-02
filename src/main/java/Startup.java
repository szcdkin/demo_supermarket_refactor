import demo.supermarket.*;
import demo.task.BuyTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Startup {

    private static Logger logger = LoggerFactory.getLogger(Startup.class);

    /**
     * CPU核数
     */
    private final int cpuCores = Runtime.getRuntime().availableProcessors();

    /**
     * 任务线程池
     */
    private ScheduledExecutorService executorService = null;

    /**
     * 超市数据存取接口
     */
    private Supermarket supermarket = null;

    /**
     * 收银台列表
     */
    private List<Cashier> cashiers;

    /**
     * 收银台选择器
     */
    private CashierSelector cashierSelector = null;

    public Startup() {
        this.init();
    }

    private void init() {
        if (logger.isInfoEnabled()) {
            logger.info("初始化数据");
        }

        // 初始化超市
        supermarket = new Supermarket();

        // 初始化收银台
        cashiers = new ArrayList<Cashier>();
        for (int i = 1; i <= Cashier.CASHIER_NUM; i++) {
            cashiers.add(new Cashier(i, supermarket));
        }

        // 初始化收银台选择器
        cashierSelector = new CashierSelector() {
            public Cashier select(CustomerOrder order, List<Cashier> cashiers) {
                // 缺省按求模的方式把订单均匀分配到各个收银台队列
                return cashiers.get(order.getOrderId() % cashiers.size());
            }
        };

        // 初始化任务线程池
        executorService = Executors.newScheduledThreadPool(cpuCores, new ThreadFactory() {
            private AtomicInteger threadNum = new AtomicInteger(1);

            public Thread newThread(Runnable r) {
                String threadName = String.format("demo-thread-%d", this.threadNum.getAndIncrement());
                Thread thread = new Thread(r, threadName);
                return thread;
            }
        });

        if (logger.isInfoEnabled()) {
            logger.info("初始化数据完毕");
        }
    }

    private Stat getStat() {
        if (logger.isInfoEnabled()) {
            logger.info("开始购买商品");
        }

        // 开始购买时间
        long startSell = System.currentTimeMillis();

        try {
            // 下一次购买推迟时间
            int buyDelay = 0;

            while (supermarket.getSaleableGoodsTotal() > 0) {
                if (buyDelay > 0) {
                    Thread.sleep(buyDelay);
                }

                // 购买商品
                Buyer buyer = new Buyer(supermarket);

                // 购买任务
                BuyTask buyTask = new BuyTask(executorService, buyer, cashiers, cashierSelector);

                // 提交购买任务
                executorService.schedule(buyTask, 0, TimeUnit.SECONDS);

                buyDelay = getRandom();
            }

            if (logger.isInfoEnabled()) {
                logger.info("等待任务结束");
            }

            // 等待任务结束
            executorService.shutdown();
            executorService.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 卖完时间
        long soldOut = System.currentTimeMillis();

        if (logger.isInfoEnabled()) {
            logger.info("汇总结果数据");
        }

        Stat stat = new Stat();
        stat.setCustomerWaitAverageTime(supermarket.statCustomerWaitAverageTime());
        stat.setGoodsSoldAverageTime(supermarket.statGoodsSoldAverageTime(startSell));
        stat.setSoldOutTotalTime(soldOut - startSell);
        for (Cashier cashier : cashiers) {
            stat.getCashierProcessOrderNum().put(cashier.getCashierId(), cashier.getProcessOrdersNum());
        }
        return stat;
    }

    private int getRandom() {
        Random random = new Random();
        return random.nextInt(3) + 1;
    }

    public static void main(String[] args) throws Exception {
        Startup startup = new Startup();
        Stat stat = startup.getStat();
        // 打印统计数据
        if (logger.isInfoEnabled()) {
            logger.info("顾客平均等待时间 {} ms", stat.getCustomerWaitAverageTime());
            logger.info("商品平均售出时间 {} ms", stat.getGoodsSoldAverageTime());
            logger.info("商品全部售出总时间 {} ms", stat.getSoldOutTotalTime());
            for (Integer cashierId : stat.getCashierProcessOrderNum().keySet()) {
                logger.info("收银台 {} 共处理 {} 个客户订单", cashierId, stat.getCashierProcessOrderNum().get(cashierId));
            }
        }
    }
}
