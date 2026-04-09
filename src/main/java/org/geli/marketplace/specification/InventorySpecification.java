package org.geli.marketplace.specification;

import org.geli.marketplace.model.InventoryModel;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InventorySpecification {
    public static Specification<InventoryModel> filter(
            LocalDate startDate, LocalDate endDate,
            LocalDate startModDate, LocalDate endModDate,
            Integer minStock, Integer maxStock) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

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
                predicates.add(cb.greaterThanOrEqualTo(root.get("quantity"), minStock));
            }
            if (maxStock != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("quantity"), maxStock));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
