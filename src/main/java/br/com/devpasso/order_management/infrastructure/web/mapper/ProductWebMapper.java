package br.com.devpasso.order_management.infrastructure.web.mapper;

import br.com.devpasso.order_management.domain.model.Product;
import br.com.devpasso.order_management.infrastructure.web.response.ProductResponse;
import org.springframework.stereotype.Component;

@Component
public class ProductWebMapper {

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.id(),
                product.name(),
                product.description(),
                product.price(),
                product.stockQuantity(),
                product.createdAt()
        );
    }
}
