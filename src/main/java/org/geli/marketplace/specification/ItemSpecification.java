package org.geli.marketplace.specification;

import org.geli.marketplace.model.ItemModel;
import org.geli.marketplace.model.VariantModel;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ItemSpecification {
    public static Specification<ItemModel> filter(
            String search,
            LocalDate startDate, LocalDate endDate,
            LocalDate startModDate, LocalDate endModDate,
            BigDecimal minPrice, BigDecimal maxPrice,
            Long minStock, Long maxStock) {
        
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isEmpty()) {
                String likePattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("itemName")), likePattern),
                        cb.like(cb.lower(root.get("description")), likePattern)
                ));
            }

            if (startDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), startDate.atStartOfDay()));
            }
            if (endDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), endDate.plusDays(1).atStartOfDay()));
            }

            if (startModDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("modifiedDate"), startModDate.atStartOfDay()));
            }
            if (endModDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("modifiedDate"), endModDate.plusDays(1).atStartOfDay()));
            }

            if (minStock != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("totalStock"), minStock));
            }
            if (maxStock != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("totalStock"), maxStock));
            }

            if (minPrice != null || maxPrice != null) {
                // Join variants to check their prices
                Join<ItemModel, VariantModel> variantJoin = root.join("variants", JoinType.LEFT);
                if (minPrice != null) {
                    predicates.add(cb.greaterThanOrEqualTo(variantJoin.get("price"), minPrice));
                }
                if (maxPrice != null) {
                    predicates.add(cb.lessThanOrEqualTo(variantJoin.get("price"), maxPrice));
                }
                query.distinct(true); 
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
