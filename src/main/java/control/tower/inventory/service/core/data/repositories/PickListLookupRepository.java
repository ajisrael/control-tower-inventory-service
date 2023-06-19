package control.tower.inventory.service.core.data.repositories;

import control.tower.inventory.service.core.data.entities.PickListLookupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickListLookupRepository extends JpaRepository<PickListLookupEntity, String> {

    PickListLookupEntity findByPickId(String pickId);
}
