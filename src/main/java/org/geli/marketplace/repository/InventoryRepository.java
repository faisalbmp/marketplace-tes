package org.geli.marketplace.repository;

import org.geli.marketplace.model.InventoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryModel, Long> {
    Optional<InventoryModel> findByVariantId(Long variantId);
}
