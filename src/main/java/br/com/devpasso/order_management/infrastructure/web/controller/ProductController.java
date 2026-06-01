package br.com.devpasso.order_management.infrastructure.web.controller;

import br.com.devpasso.order_management.application.usecase.product.CreateProductUseCase;
import br.com.devpasso.order_management.application.usecase.product.DeleteProductUseCase;
import br.com.devpasso.order_management.application.usecase.product.GetProductByIdUseCase;
import br.com.devpasso.order_management.application.usecase.product.ListProductsUseCase;
import br.com.devpasso.order_management.application.usecase.product.UpdateProductStockUseCase;
import br.com.devpasso.order_management.application.usecase.product.UpdateProductUseCase;
import br.com.devpasso.order_management.domain.model.Product;
import br.com.devpasso.order_management.infrastructure.web.mapper.ProductWebMapper;
import br.com.devpasso.order_management.infrastructure.web.request.CreateProductRequest;
import br.com.devpasso.order_management.infrastructure.web.request.UpdateProductRequest;
import br.com.devpasso.order_management.infrastructure.web.request.UpdateStockRequest;
import br.com.devpasso.order_management.infrastructure.web.response.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Gerenciamento de produtos")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductByIdUseCase getProductByIdUseCase;
    private final ListProductsUseCase listProductsUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final ProductWebMapper mapper;

    @PostMapping
    @Operation(summary = "Cria um novo produto")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody CreateProductRequest request) {
        Product created = createProductUseCase.execute(mapper.toCommand(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(created));
    }

    @GetMapping
    @Operation(summary = "Lista todos os produtos paginados")
    public ResponseEntity<Page<ProductResponse>> listAll(Pageable pageable) {
        Page<ProductResponse> result = listProductsUseCase
                .execute(pageable)
                .map(mapper::toResponse);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca produto por ID")
    public ResponseEntity<ProductResponse> getById(@PathVariable UUID id) {
        Product product = getProductByIdUseCase.execute(id);
        return ResponseEntity.ok(mapper.toResponse(product));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um produto")
    public ResponseEntity<ProductResponse> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        Product updated = updateProductUseCase.execute(id, mapper.toCommand(request));
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Atualiza estoque do produto")
    public ResponseEntity<ProductResponse> updateStock(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateStockRequest request
    ) {
        Product updated = updateProductStockUseCase.execute(id, request.quantity());
        return ResponseEntity.ok(mapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove um produto")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteProductUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }
}
