package control.tower.inventory.service.command.rest;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
public class CreateInventoryItemRestModel {

    @NotBlank(message = "ProductId is a required field")
    private String productId;
    @NotBlank(message = "LocationId is a required field")
    private String locationId;
    @NotBlank(message = "BinId is a required field")
    private String binId;
}
