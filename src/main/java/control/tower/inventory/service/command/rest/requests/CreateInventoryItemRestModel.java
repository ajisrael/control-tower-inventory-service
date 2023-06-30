package control.tower.inventory.service.command.rest.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CreateInventoryItemRestModel {

    @NotBlank(message = "ProductId is a required field")
    private String productId;
    @NotBlank(message = "LocationId is a required field")
    private String locationId;
    @NotBlank(message = "BinId is a required field")
    private String binId;
}
