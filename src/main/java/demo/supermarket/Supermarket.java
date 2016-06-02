package demo.supermarket;

import demo.supermarket.exception.InventoryShortageException;
import demo.supermarket.exception.OrderHasFinishedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 超市数据存取接口
 */
public class Supermarket {

    /**
     * 商品列表
     */
    private List<Goods> listGoods;

    /**
     * 客户订单列表
     */
    private ConcurrentHashMap<Integer, CustomerOrder> orders;


    public Supermarket() {
        this.init();
    }

    /**
     * 统计顾客平均等待时间
     * @return
     */
    public long statCustomerWaitAverageTime() {
        long totalWaitTime = 0;
        int totalOrders = 0;
        for (CustomerOrder order : this.orders.values()) {
            if (order.getCreated() > 0 && order.getFinished() > 0 && order.getFinished() > order.getCreated()) {
                totalWaitTime += (order.getFinished() - order.getCreated());
                totalOrders++;
            }
        }
        return totalWaitTime / (totalOrders == 0 ? 1 : totalOrders);
    }

    /**
     * 统计商品售出平均时间
     * @param startSell
     * @return
     */
    public long statGoodsSoldAverageTime(long startSell) {
        long totalSoldTime = 0;
        int totalOrders = 0;
        for (CustomerOrder order : this.orders.values()) {
            if (order.getCreated() > 0 && order.getFinished() > 0 && order.getFinished() > startSell) {
                totalSoldTime += (order.getFinished() - startSell);
                totalOrders++;
            }
        }
        return totalSoldTime / (totalOrders == 0 ? 1 : totalOrders);
    }

    /**
     * 随机挑选一商品
     * @return
     */
    public Goods getRandomGoods() {
        // 商品索引数组
        int indexes[] = new int[listGoods.size()];
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = i;
        }

        Goods goods = null;
        Random random = new Random();
        int nextBound = indexes.length;

        while (nextBound > 0) {
            // 随机数种子
            random.setSeed(System.currentTimeMillis());

            int rand = random.nextInt(nextBound);
            int index = indexes[rand];
            goods = listGoods.get(index);
            // 商品有库存,结束查找
            if (goods.getInventory().get() > 0) {
                break;
            }

            // 商品没有库存
            goods = null;

            // 随机值范围减1
            nextBound--;

            // 没有库存的商品索引交换到索引数组尾部, 确保下一次挑选不会被再次被挑选到
            if (nextBound != rand) {
                int tail = indexes[nextBound];
                indexes[nextBound] = indexes[rand];
                indexes[rand] = tail;
            }
        }
        return goods;
    }


    /**
     * 获取可售的商品数
     * @return
     */
    public int getSaleableGoodsTotal() {
        int count = 0;
        for (Goods goods : this.listGoods) {
            count += goods.getInventory().get();
        }
        return count;
    }

    /**
     * 订单支付
     * @param order
     * @throws OrderHasFinishedException
     * @throws InventoryShortageException
     */
    public void payOrder(CustomerOrder order) throws OrderHasFinishedException, InventoryShortageException {
        // 订单已完成
        if (order.getStatus() == CustomerOrder.ORDER_FINISHED) {
            throw new OrderHasFinishedException("");
        }
        // 扣减商品库存
        if (order.getGoods().getInventory().decrementAndGet() < 0) {
            throw new InventoryShortageException((""));
        }
        // 修改订单状态为完成状态
        order.setStatus(CustomerOrder.ORDER_FINISHED);
        order.setFinished(System.currentTimeMillis());
    }

    /**
     * 保存订单
     * @param order
     */
    public void insertOrder(CustomerOrder order) {
        this.orders.put(order.getOrderId(), order);
    }

    /**
     * 初始化超市
     * @return
     */
    private void init() {
        // 初始化订单列表
        this.orders = new ConcurrentHashMap<Integer, CustomerOrder>();

        // 初始化商品列表
        this.listGoods = new ArrayList<Goods>();
        String names[] = {"Apple", "Macbook", "Cookie"};
        for (int i = 0; i < names.length; i++) {
            listGoods.add(new Goods(i + 1, names[i], new AtomicInteger(15)));
        }
    }

    public List<Goods> getListGoods() {
        return listGoods;
    }

    public ConcurrentHashMap<Integer, CustomerOrder> getOrders() {
        return orders;
    }
}
