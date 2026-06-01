package br.com.devpasso.order_management.application.port.out;

import br.com.devpasso.order_management.domain.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryPort {

    Page<Product> findAll(Pageable pageable);

}
