package control.tower.inventory.service.core.data.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventoryitemassignedtopicklist")
public class PickItemEntity implements Serializable {

    private static final long serialVersionUID = -3787108556148621736L;

    @Id
    @Column(unique = true)
    private String sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pick_id")
    private PickListEntity pickList;

    private boolean isSkuPicked;
}
