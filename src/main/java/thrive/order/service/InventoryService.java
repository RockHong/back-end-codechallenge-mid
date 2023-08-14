package thrive.order.service;

public interface InventoryService {
    int getStockQuantity(Long productId);

    void reduceStockQuantity(Long productId, int quantity);
}
