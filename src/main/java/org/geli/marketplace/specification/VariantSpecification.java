package org.geli.marketplace.specification;

import org.geli.marketplace.model.VariantModel;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VariantSpecification {
    public static Specification<VariantModel> filter(
            String search,
            LocalDate startDate, LocalDate endDate,
            LocalDate startModDate, LocalDate endModDate,
            BigDecimal minPrice, BigDecimal maxPrice,
            Integer minStock, Integer maxStock) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (search != null && !search.isEmpty()) {
                String likePattern = "%" + search.toLowerCase() + "%";
                predicates.add(cb.or(
                        cb.like(cb.lower(root.get("sku")), likePattern),
                        cb.like(cb.lower(root.get("variantName")), likePattern)
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

            if (minPrice != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
