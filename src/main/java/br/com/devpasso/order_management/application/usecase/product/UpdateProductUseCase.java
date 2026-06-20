package br.com.devpasso.order_management.application.usecase.product;

import br.com.devpasso.order_management.application.dto.command.UpdateProductCommand;
import br.com.devpasso.order_management.application.port.out.ProductRepositoryPort;
import br.com.devpasso.order_management.domain.exception.BusinessException;
import br.com.devpasso.order_management.domain.exception.ResourceNotFoundException;
import br.com.devpasso.order_management.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateProductUseCase {

    private final ProductRepositoryPort repository;

    public Product execute(UUID id, UpdateProductCommand command) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado: " + id));

        if (!existing.name().equalsIgnoreCase(command.name()) && repository.existsByName(command.name())) {
            throw new BusinessException("Nome '" + command.name() + "' ja esta em uso");
        }

        Product updated = new Product(
                existing.id(),
                command.name(),
                command.description(),
                command.price(),
                existing.stockQuantity(),
                existing.createdAt()
        );

        return repository.save(updated);
    }
}
