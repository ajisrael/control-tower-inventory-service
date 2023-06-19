package control.tower.inventory.service.core.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "inventoryitemlookup")
public class InventoryItemLookupEntity implements Serializable {

    private static final long serialVersionUID = -4787108556148621714L;

    @Id
    @Column(unique = true)
    private String sku;
}
