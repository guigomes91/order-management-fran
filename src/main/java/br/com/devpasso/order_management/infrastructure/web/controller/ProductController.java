package br.com.devpasso.order_management.infrastructure.web.controller;

import br.com.devpasso.order_management.application.usecase.product.ListProductsUseCase;
import br.com.devpasso.order_management.infrastructure.web.mapper.ProductWebMapper;
import br.com.devpasso.order_management.infrastructure.web.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Gerenciamento de produtos")
public class ProductController {

    private final ListProductsUseCase listProductsUseCase;
    private final ProductWebMapper mapper;

    @GetMapping
    @Operation(summary = "Lista todos os produtos paginados")
    public ResponseEntity<Page<ProductResponse>> listAll(Pageable pageable) {
        Page<ProductResponse> result = listProductsUseCase
                .execute(pageable)
                .map(mapper::toResponse);

        return ResponseEntity.ok(result);
    }
}
