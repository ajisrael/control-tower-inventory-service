package control.tower.inventory.service.core.data.repositories;

import control.tower.inventory.service.core.data.entities.PickItemLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickItemLookupRepository extends JpaRepository<PickItemLookupEntity, String> {

    PickItemLookupEntity findBySku(String sku);
}
