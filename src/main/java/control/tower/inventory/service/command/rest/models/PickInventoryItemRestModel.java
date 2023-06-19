package control.tower.inventory.service.command.rest.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class PickInventoryItemRestModel {

    @NotEmpty(message = "sku cannot be empty")
    private String sku;
    @NotEmpty(message = "pickId cannot be empty")
    private String pickId;
}
