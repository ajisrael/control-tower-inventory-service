package control.tower.inventory.service.command.rest.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreatePickListRestModel {

    @NotNull(message = "Sku List is required")
    @NotEmpty(message = "skuList cannot be empty")
    private List<String> skuList;
    @NotNull(message = "Pick by date is required")
    private Date pickByDate;
}
