package br.com.devpasso.order_management.infrastructure.web.mapper;

import br.com.devpasso.order_management.infrastructure.persistence.entity.Product;
import br.com.devpasso.order_management.infrastructure.web.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductWebMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCreatedAt()
        );
    }
}