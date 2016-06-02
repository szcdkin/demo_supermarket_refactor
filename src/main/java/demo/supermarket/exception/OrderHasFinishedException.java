package demo.supermarket.exception;

/**
 * 订单已完成异常
 */
public class OrderHasFinishedException extends Exception {

    public OrderHasFinishedException(String msg) {
        super(msg);
    }
}
