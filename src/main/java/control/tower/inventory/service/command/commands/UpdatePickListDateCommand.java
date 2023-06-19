package control.tower.inventory.service.command.commands;

import lombok.Builder;
import lombok.Getter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.Date;

import static control.tower.core.utils.Helper.throwExceptionIfParameterIsEmpty;
import static control.tower.core.utils.Helper.throwExceptionIfParameterIsNull;

@Getter
@Builder
public class UpdatePickListDateCommand {

    @TargetAggregateIdentifier
    private String pickId;
    private Date pickByDate;

    public void validate() {
        throwExceptionIfParameterIsEmpty(this.getPickId(), "Pick by date cannot by null");
        throwExceptionIfParameterIsNull(this.getPickByDate(), "Pick by date cannot by null");
    }
}
