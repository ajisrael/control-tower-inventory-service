package control.tower.inventory.service.command.rest.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@NoArgsConstructor
public class UpdatePickListDateRestModel {

    @NotEmpty(message = "PickId is a required field")
    private String pickId;
    @NotNull(message = "PickByDate is a required field")
    private Date pickByDate;
}
