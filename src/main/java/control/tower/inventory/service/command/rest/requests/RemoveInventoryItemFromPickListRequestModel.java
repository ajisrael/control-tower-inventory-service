package control.tower.inventory.service.command.rest.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RemoveInventoryItemFromPickListRequestModel {

    @NotBlank(message = "Sku is a required field")
    private String sku;
    @NotBlank(message = "PickId is a required field")
    private String pickId;
}
