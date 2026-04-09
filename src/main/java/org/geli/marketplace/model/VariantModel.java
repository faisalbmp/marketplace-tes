package org.geli.marketplace.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

import org.hibernate.annotations.Formula;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "variant")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VariantModel extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemModel item;

    @Column(name = "sku", unique = true, nullable = false)
    private String sku; 

    @Column(name = "variant_name")
    private String variantName; 

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Formula("(SELECT COALESCE(inv.quantity, 0) FROM inventory inv WHERE inv.variant_id = id)")
    private Integer totalStock;
}