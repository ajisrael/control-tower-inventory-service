package control.tower.inventory.service.core.data.repositories;

import control.tower.inventory.service.core.data.entities.InventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemRepository extends JpaRepository<InventoryItemEntity, String> {

    InventoryItemEntity findBySku(String sku);
}
