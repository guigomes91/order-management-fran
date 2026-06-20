package br.com.devpasso.order_management.infrastructure.web.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateStockRequest(
        @NotNull
        @PositiveOrZero
        Integer quantity
) {
}
