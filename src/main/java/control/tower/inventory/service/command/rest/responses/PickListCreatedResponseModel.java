package control.tower.inventory.service.command.rest.responses;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PickListCreatedResponseModel {

    private String pickId;
}
