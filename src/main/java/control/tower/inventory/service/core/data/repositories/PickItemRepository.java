package control.tower.inventory.service.core.data.repositories;

import control.tower.inventory.service.core.data.entities.PickItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickItemRepository extends JpaRepository<PickItemEntity, String> {

    PickItemEntity findBySku(String sku);
}
