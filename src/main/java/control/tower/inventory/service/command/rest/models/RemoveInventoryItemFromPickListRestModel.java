package control.tower.inventory.service.command.rest.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RemoveInventoryItemFromPickListRestModel {

    @NotBlank(message = "Sku is a required field")
    private String sku;
    @NotBlank(message = "PickId is a required field")
    private String pickId;
}
