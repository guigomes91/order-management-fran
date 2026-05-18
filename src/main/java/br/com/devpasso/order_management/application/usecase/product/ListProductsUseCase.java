package br.com.devpasso.order_management.application.usecase.product;

import br.com.devpasso.order_management.application.port.out.ProductRepositoryPort;
import br.com.devpasso.order_management.infrastructure.persistence.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListProductsUseCase {

    private final ProductRepositoryPort repository;

    public Page<Product> execute(Pageable pageable) {
        return repository.findAll(pageable);
    }

}