package control.tower.inventory.service.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, String> {

    InventoryItemEntity findBySku(String sku);
}
