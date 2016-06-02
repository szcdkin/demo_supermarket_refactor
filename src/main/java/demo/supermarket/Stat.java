package demo.supermarket;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计结果
 */
public class Stat {

    /**
     * 顾客平均等待时间
     */
    private long customerWaitAverageTime;

    /**
     * 商品平均售出时间
     */
    private long goodsSoldAverageTime;

    /**
     * 商品全部售出总时间
     */
    private long soldOutTotalTime;

    /**
     * 收银台处理订单数
     */
    private Map<Integer, Integer> cashierProcessOrderNum = new HashMap<Integer, Integer>();

    public long getCustomerWaitAverageTime() {
        return customerWaitAverageTime;
    }

    public void setCustomerWaitAverageTime(long customerWaitAverageTime) {
        this.customerWaitAverageTime = customerWaitAverageTime;
    }

    public long getGoodsSoldAverageTime() {
        return goodsSoldAverageTime;
    }

    public void setGoodsSoldAverageTime(long goodsSoldAverageTime) {
        this.goodsSoldAverageTime = goodsSoldAverageTime;
    }

    public long getSoldOutTotalTime() {
        return soldOutTotalTime;
    }

    public void setSoldOutTotalTime(long soldOutTotalTime) {
        this.soldOutTotalTime = soldOutTotalTime;
    }

    public Map<Integer, Integer> getCashierProcessOrderNum() {
        return cashierProcessOrderNum;
    }

    public void setCashierProcessOrderNum(Map<Integer, Integer> cashierProcessOrderNum) {
        this.cashierProcessOrderNum = cashierProcessOrderNum;
    }
}
