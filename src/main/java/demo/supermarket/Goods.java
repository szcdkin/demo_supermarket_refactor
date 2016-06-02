package demo.supermarket;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 商品
 */
public class Goods {

    /**
     * 商品id
     */
    private int goodsId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品库存值
     */
    private AtomicInteger inventory;

    public Goods(int goodsId, String name, AtomicInteger inventory) {
        this.goodsId = goodsId;
        this.name = name;
        this.inventory = inventory;
    }

    public int getGoodsId() {
        return goodsId;
    }

    public String getName() {
        return name;
    }

    public AtomicInteger getInventory() {
        return inventory;
    }
}
