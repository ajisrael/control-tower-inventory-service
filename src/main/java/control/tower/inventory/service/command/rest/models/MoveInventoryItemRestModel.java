package control.tower.inventory.service.command.rest.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MoveInventoryItemRestModel {

    @NotBlank(message = "Sku is a required field")
    private String sku;
    @NotBlank(message = "LocationId is a required field")
    private String locationId;
    @NotBlank(message = "BinId is a required field")
    private String binId;
}
