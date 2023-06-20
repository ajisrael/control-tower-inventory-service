package control.tower.inventory.service.core.data.repositories;

import control.tower.inventory.service.core.data.entities.InventoryItemHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemHistoryRepository extends JpaRepository<InventoryItemHistoryEntity, String> {

    InventoryItemHistoryEntity findBySku(String sku);
}
