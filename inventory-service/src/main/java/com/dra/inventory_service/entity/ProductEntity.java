package com.dra.inventory_service.entity;

import java.util.List;

import com.dra.inventory_service.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "product")
@Getter
@Setter
public class ProductEntity extends CommonEntity{

    @Column(name = "name")
    private String name;

    @Column(name = "product_code")
    private String productCode;

    @Enumerated(EnumType.STRING)
    @Column(name= "status")
    private ProductStatus status;

    @Column(name = "price")
    private Double price;

    @OneToOne(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonBackReference
    private InventoryEntity inventoryData;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @JsonBackReference
    private List<InventoryReservationEntity> inventoryReservations;

}
