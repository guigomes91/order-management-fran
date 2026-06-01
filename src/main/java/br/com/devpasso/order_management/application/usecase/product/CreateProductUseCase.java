package br.com.devpasso.order_management.application.usecase.product;

import br.com.devpasso.order_management.application.dto.command.CreateProductCommand;
import br.com.devpasso.order_management.application.port.out.ProductRepositoryPort;
import br.com.devpasso.order_management.domain.exception.BusinessException;
import br.com.devpasso.order_management.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepositoryPort repository;

    public Product execute(CreateProductCommand command) {
        if (repository.existsByName(command.name())) {
            throw new BusinessException("Produto com nome '" + command.name() + "' ja existe");
        }

        Product product = new Product(
                null,
                command.name(),
                command.description(),
                command.price(),
                command.stockQuantity(),
                LocalDateTime.now()
        );

        return repository.save(product);
    }
}
