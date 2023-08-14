package thrive.order.service;

import org.springframework.stereotype.Service;

@Service
public class InventoryServiceImpl implements InventoryService{
    @Override
    public int getStockQuantity(Long productId) {
        // TODO
        return 999;
    }

    @Override
    public void reduceStockQuantity(Long productId, int quantity) {
        // TODO
    }
}
