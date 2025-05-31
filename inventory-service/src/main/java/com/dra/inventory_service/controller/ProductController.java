package com.dra.inventory_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dra.inventory_service.dto.ProductData;
import com.dra.inventory_service.dto.request.CreateProductData;
import com.dra.inventory_service.dto.request.ProductSearchData;
import com.dra.inventory_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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

    @Value("${spring.appData.pageMaxSize}")
    private int pageMaxSize;

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductData>> getProductList(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String productCode,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer pageSize
    ) {
        ProductSearchData searchData = ProductSearchData.builder()
                                    .page(page==null?0:page-1)
                                    .pageSize(pageSize==null?pageMaxSize:pageSize)
                                    .productCode(productCode)
                                    .name(name)
                                    .status(status)
                                    .build();
        Page<ProductData> dataList = this.productService.getProductList(searchData);
        return ResponseEntity.status(HttpStatus.OK).body(dataList);
    }

    @GetMapping("{code}")
    public ResponseEntity<ProductData> getProduct(@PathVariable(name = "code") String productCode) {
        ProductData data = this.productService.getProduct(productCode);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }

    @PostMapping
    public ResponseEntity<ProductData> createProduct(@RequestBody @Valid CreateProductData productData) {
        ProductData data = this.productService.createProduct(productData);
        return ResponseEntity.status(HttpStatus.CREATED).body(data);
    }

    @PatchMapping("{code}")
    public ResponseEntity<ProductData> partialUpdateProduct(@PathVariable String code, @RequestBody @Valid CreateProductData productData) {
        ProductData data = this.productService.partialUpdateProduct(code, productData);
        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
    

}
