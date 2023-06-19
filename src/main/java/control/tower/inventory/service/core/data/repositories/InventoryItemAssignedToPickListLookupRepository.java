package control.tower.inventory.service.core.data.repositories;

import control.tower.inventory.service.core.data.entities.InventoryItemAssignedToPickListLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemAssignedToPickListLookupRepository extends JpaRepository<InventoryItemAssignedToPickListLookupEntity, String> {

    InventoryItemAssignedToPickListLookupEntity findBySku(String sku);
}
