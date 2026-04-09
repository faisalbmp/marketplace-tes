package org.geli.marketplace.repository;

import org.geli.marketplace.model.VariantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface VariantRepository extends JpaRepository<VariantModel, Long>, JpaSpecificationExecutor<VariantModel> {
}
