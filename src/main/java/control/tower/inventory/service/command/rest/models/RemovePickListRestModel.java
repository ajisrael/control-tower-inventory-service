package control.tower.inventory.service.command.rest.models;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class RemovePickListRestModel {

    @NotBlank(message = "PickId is a required field")
    private String pickId;
}
