package com.ecommerce.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="addresses")
public class Address
{

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long addressId;
   
    private String buildingName;
    private String street;
    private String city;
    private String state;
    private String country;
    private String pincode;
}
