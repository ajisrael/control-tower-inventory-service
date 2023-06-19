package control.tower.inventory.service.core.data.repositories;

import control.tower.inventory.service.core.data.entities.PickListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PickListRepository extends JpaRepository<PickListEntity, String> {

    PickListEntity findByPickId(String pickId);
}
