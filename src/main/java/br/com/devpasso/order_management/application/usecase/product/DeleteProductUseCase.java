package br.com.devpasso.order_management.application.usecase.product;

import br.com.devpasso.order_management.application.port.out.ProductRepositoryPort;
import br.com.devpasso.order_management.domain.exception.ResourceNotFoundException;
import br.com.devpasso.order_management.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProductRepositoryPort repository;

    public void execute(UUID id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto nao encontrado: " + id));

        repository.delete(product);
    }
}
