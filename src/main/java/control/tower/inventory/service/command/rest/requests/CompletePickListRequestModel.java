package control.tower.inventory.service.command.rest.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class CompletePickListRequestModel {

    @NotBlank(message = "PickId is a required field")
    private String pickId;
}
