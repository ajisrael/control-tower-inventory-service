package control.tower.inventory.service.core.events;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class PickListDateUpdatedEvent {

    private String pickId;
    private Date pickByDate;
}
