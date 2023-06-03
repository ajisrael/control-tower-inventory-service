package control.tower.inventory.service.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateInventoryItemRestModel {

    private String productId;
    private String locationId;
    private String binId;
}
