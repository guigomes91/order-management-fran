package br.com.devpasso.order_management.application.port.out;

import br.com.devpasso.order_management.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort {

    Product save(Product product);

    Optional<Product> findById(UUID id);

    boolean existsByName(String name);

    Page<Product> findAll(Pageable pageable);

    void delete(Product product);

}
