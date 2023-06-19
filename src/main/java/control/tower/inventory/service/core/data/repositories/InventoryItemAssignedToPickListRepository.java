package control.tower.inventory.service.core.data.repositories;

import control.tower.inventory.service.core.data.entities.InventoryItemAssignedToPickListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemAssignedToPickListRepository extends JpaRepository<InventoryItemAssignedToPickListEntity, String> {

    InventoryItemAssignedToPickListEntity findBySku(String sku);
}
