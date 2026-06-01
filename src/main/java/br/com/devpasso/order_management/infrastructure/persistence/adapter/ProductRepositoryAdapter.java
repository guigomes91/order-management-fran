package br.com.devpasso.order_management.infrastructure.persistence.adapter;

import br.com.devpasso.order_management.application.port.out.ProductRepositoryPort;
import br.com.devpasso.order_management.domain.model.Product;
import br.com.devpasso.order_management.infrastructure.persistence.mapper.ProductPersistenceMapper;
import br.com.devpasso.order_management.infrastructure.persistence.repository.ProductJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductJpaRepository repository;
    private final ProductPersistenceMapper mapper;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDomain);
    }

}
