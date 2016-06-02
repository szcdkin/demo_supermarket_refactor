package demo.supermarket.exception;

/**
 * 库存不足异常
 */
public class InventoryShortageException extends Exception {
    public InventoryShortageException(String msg) {
        super(msg);
    }
}
