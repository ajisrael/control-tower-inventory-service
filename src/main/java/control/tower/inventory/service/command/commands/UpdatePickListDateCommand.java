package control.tower.inventory.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;

import static control.tower.core.utils.Helper.throwExceptionIfParameterIsEmpty;
import static control.tower.core.utils.Helper.throwExceptionIfParameterIsNull;
import static control.tower.inventory.service.core.constants.ExceptionMessages.PICK_BY_DATE_CANNOT_BE_NULL;
import static control.tower.inventory.service.core.constants.ExceptionMessages.PICK_ID_CANNOT_BE_EMPTY;

@Getter
@Builder
public class UpdatePickListDateCommand {

    @TargetAggregateIdentifier
    private String pickId;
    private Date pickByDate;

    public void validate() {
        throwExceptionIfParameterIsEmpty(this.getPickId(), PICK_ID_CANNOT_BE_EMPTY);
        throwExceptionIfParameterIsNull(this.getPickByDate(), PICK_BY_DATE_CANNOT_BE_NULL);
    }
}
