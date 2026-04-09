package org.geli.marketplace.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.Formula;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "item")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ItemModel extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(name = "description")
    private String description;

    @Formula("(SELECT COALESCE(SUM(inv.quantity), 0) FROM variant v LEFT JOIN inventory inv ON v.id = inv.variant_id WHERE v.item_id = id)")
    private Long totalStock;

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("item")
    private java.util.List<org.geli.marketplace.model.VariantModel> variants;
}