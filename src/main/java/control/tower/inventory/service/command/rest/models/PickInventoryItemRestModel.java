package control.tower.inventory.service.command.rest.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class PickInventoryItemRestModel {

    @NotEmpty(message = "Sku is a required field")
    private String sku;
    @NotEmpty(message = "PickId is a required field")
    private String pickId;
}
