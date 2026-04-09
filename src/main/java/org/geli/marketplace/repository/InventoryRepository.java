package org.geli.marketplace.repository;

import org.geli.marketplace.model.InventoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryModel, Long>, JpaSpecificationExecutor<InventoryModel> {
    Optional<InventoryModel> findByVariantId(Long variantId);
}
