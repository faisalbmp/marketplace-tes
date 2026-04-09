package org.geli.marketplace.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "public", name = "inventory")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class InventoryModel extends AuditModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variant_id", nullable = false, unique = true)
    private VariantModel variant;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // THIS IS THE MAGIC FOR CONCURRENCY PROTECTION
    @Version
    @Column(name = "version")
    private Long version;
}