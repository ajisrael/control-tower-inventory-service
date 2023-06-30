package control.tower.inventory.service.command.rest.responses;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class InventoryItemCreatedResponseModel {

    private String sku;
}
