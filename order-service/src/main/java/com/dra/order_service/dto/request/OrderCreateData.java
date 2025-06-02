package com.dra.order_service.dto.request;

import java.util.List;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderCreateData {

    @NotNull(message = "Products are required.")
    private List<ProductCreateData> products;

}
