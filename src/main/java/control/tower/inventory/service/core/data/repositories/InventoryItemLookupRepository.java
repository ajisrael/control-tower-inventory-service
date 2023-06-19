package control.tower.inventory.service.core.data.repositories;

import control.tower.inventory.service.core.data.entities.InventoryItemLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryItemLookupRepository extends JpaRepository<InventoryItemLookupEntity, String> {

    InventoryItemLookupEntity findBySku(String sku);
}
