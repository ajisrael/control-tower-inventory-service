package control.tower.inventory.service.core.data.converters;

import control.tower.inventory.service.core.data.dtos.PickListDto;
import control.tower.inventory.service.core.data.entities.PickItemEntity;
import control.tower.inventory.service.core.data.entities.PickListEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PickListDtoToPickListEntityConverter {

    public PickListEntity convert(PickListDto pickListDto) {
        PickListEntity pickListEntity = new PickListEntity();
        pickListEntity.setPickId(pickListDto.getPickId());
        pickListEntity.setPickByDate(pickListDto.getPickByDate());
        pickListEntity.setComplete(pickListDto.isPicked());

        List<PickItemEntity> skuList = new ArrayList<>();

        for (String sku: pickListDto.getSkuMap().keySet()) {
            skuList.add(new PickItemEntity(sku, pickListEntity, pickListDto.getSkuMap().get(sku)));
        }

        pickListEntity.setSkuList(skuList);

        return pickListEntity;
    }
}
