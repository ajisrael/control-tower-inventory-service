package control.tower.inventory.service.core.data.converters;

import control.tower.inventory.service.core.data.entities.PickItemEntity;
import control.tower.inventory.service.core.data.entities.PickListEntity;
import control.tower.inventory.service.core.data.dtos.PickListDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PickListEntityToPickListDtoConverter {
    public PickListDto convert(PickListEntity pickListEntity) {
        Map<String, Boolean> skuMap = new HashMap<>();

        for(PickItemEntity pickItemEntity : pickListEntity.getSkuList()) {
            skuMap.put(pickItemEntity.getSku(), pickItemEntity.isSkuPicked());
        }

        return new PickListDto(
                pickListEntity.getPickId(),
                skuMap,
                pickListEntity.getPickByDate(),
                pickListEntity.isComplete()
        );
    }
}
