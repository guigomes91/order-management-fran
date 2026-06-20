package br.com.devpasso.order_management.infrastructure.web.response;

public record ErrorResponse(
        String code,
        String message
) {
}
