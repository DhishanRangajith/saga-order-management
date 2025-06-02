package com.dra.inventory_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.dra.inventory_service.annotation.ProductCodeValid;
import com.dra.inventory_service.dto.request.InventorySearchData;
import com.dra.inventory_service.dto.request.ProductQuantData;
import com.dra.inventory_service.dto.response.InventoryData;
import com.dra.inventory_service.service.InventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/inventory")
@RequiredArgsConstructor
@Validated
public class InventoryController {

    @Value("${spring.appData.pageMaxSize}")
    private int pageMaxSize;

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<Page<InventoryData>> getInventoryData(
        @RequestParam(required = false) String productName,
        @RequestParam(required = false) String productCode,
        @RequestParam(required = false) String productStatus,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer pageSize
    ){
        InventorySearchData searchData = InventorySearchData.builder()
                                            .page(page==null?0:page-1)
                                            .pageSize(pageSize==null?pageMaxSize:pageSize)
                                            .productCode(productCode)
                                            .productName(productName)
                                            .productStatus(productStatus)
                                            .build();

        Page<InventoryData> dataPageList = this.inventoryService.getInventotyData(searchData);
        return ResponseEntity.status(HttpStatus.OK).body(dataPageList);
    }

    @PatchMapping("product/{productCode}/add")
    public ResponseEntity<InventoryData> addProductToInventory(@PathVariable @ProductCodeValid String productCode, @RequestBody @Valid ProductQuantData productQuantData){
        InventoryData inventoryData = this.inventoryService.addProductToInventory(productCode, productQuantData);
        return ResponseEntity.status(HttpStatus.OK).body(inventoryData);
    }

    @PatchMapping("product/{productCode}/remove")
    public ResponseEntity<InventoryData> removeProductFromInventory(@PathVariable @ProductCodeValid String productCode, @RequestBody @Valid ProductQuantData productQuantData){
        InventoryData inventoryData = this.inventoryService.removeProductFromInventory(productCode, productQuantData);
        return ResponseEntity.status(HttpStatus.OK).body(inventoryData);
    }

}
