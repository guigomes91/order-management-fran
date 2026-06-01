package br.com.devpasso.order_management.infrastructure.persistence.mapper;

import br.com.devpasso.order_management.domain.model.Product;
import br.com.devpasso.order_management.infrastructure.persistence.entity.ProductEntity;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceMapper {

    public ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.stockQuantity(),
                product.createdAt()
        );
    }

    public Product toDomain(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStockQuantity(),
                entity.getCreatedAt()
        );
    }
}
