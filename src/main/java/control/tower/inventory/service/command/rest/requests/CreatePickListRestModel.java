package control.tower.inventory.service.command.rest.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@NoArgsConstructor
public class CreatePickListRestModel {

    @NotNull(message = "SkuList is a required field")
    @NotEmpty(message = "SkuList is a required field")
    private List<String> skuList;
    @NotNull(message = "PickByDate is a required field")
    private Date pickByDate;
}
