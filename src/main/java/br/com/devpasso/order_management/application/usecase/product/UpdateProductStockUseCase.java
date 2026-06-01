package br.com.devpasso.order_management.application.usecase.product;

import br.com.devpasso.order_management.application.port.out.ProductRepositoryPort;
import br.com.devpasso.order_management.domain.exception.ResourceNotFoundException;
import br.com.devpasso.order_management.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateProductStockUseCase {

    private final ProductRepositoryPort repository;

    public Product execute(UUID id, Integer quantity) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado: " + id));

        Product updated = new Product(
                existing.id(),
                existing.name(),
                existing.description(),
                existing.price(),
                quantity,
                existing.createdAt()
        );

        return repository.save(updated);
    }
}
