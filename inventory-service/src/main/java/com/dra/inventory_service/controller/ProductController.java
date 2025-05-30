package com.dra.inventory_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dra.inventory_service.annotation.IdMatches;
import com.dra.inventory_service.annotation.ProductCreateValid;
import com.dra.inventory_service.annotation.ProductUpdateValid;
import com.dra.inventory_service.dto.ProductData;
import com.dra.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductData>> getProductList() {
        List<ProductData> dataList = this.productService.getProductList();
        return ResponseEntity.status(HttpStatus.OK).body(dataList);
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductData> getProduct(@PathVariable Long id) {
        ProductData data = this.productService.getProduct(id);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PostMapping
    public ResponseEntity<ProductData> createProduct(@RequestBody @ProductCreateValid ProductData productData) {
        ProductData data = this.productService.createProduct(productData);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @PatchMapping("{id}")
    @IdMatches
    public ResponseEntity<ProductData> partialUpdateProduct(@PathVariable Long id, @RequestBody @ProductUpdateValid ProductData productData) {
        ProductData data = this.productService.partialUpdateProduct(id, productData);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
    

}
