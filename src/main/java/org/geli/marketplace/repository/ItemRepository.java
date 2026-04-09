package org.geli.marketplace.repository;

import org.geli.marketplace.model.ItemModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel, Long>, JpaSpecificationExecutor<ItemModel> {
}
