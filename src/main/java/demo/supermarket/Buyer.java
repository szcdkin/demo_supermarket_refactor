package demo.supermarket;

public class Buyer {

    /**
     * 超市数据存取接口
     */
    private Supermarket supermarket;

    public Buyer(Supermarket supermarket) {
        this.supermarket = supermarket;
    }

    public CustomerOrder getOrder() {
        CustomerOrder order = null;

        // 挑选一商品
        Goods goods = supermarket.getRandomGoods();

        if (goods == null) {
            return order;
        }

        // 生成订单
        order = new CustomerOrder();
        order.setOrderId(CustomerOrder.orderIdGenerator.getAndIncrement());
        order.setGoods(goods);
        order.setStatus(CustomerOrder.ORDER_CREATED);
        order.setCreated(System.currentTimeMillis());
        order.setFinished(0l);

        // 保存订单
        supermarket.insertOrder(order);

        return order;
    }
}
