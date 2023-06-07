package control.tower.inventory.service.command.rest;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RemoveInventoryItemRestModel {

    @NotBlank(message = "SKU is a required field")
    private String sku;
}
