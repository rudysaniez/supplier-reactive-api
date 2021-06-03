package com.me.api.supplier.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table("address")
@NoArgsConstructor
@Data
public class AddressEntity {

	@Id
	private Integer id;
	
	@Column("address_details")
	private String address;
	
	@Column("city")
	private String city;
	
	@Column("country")
	private String country;
	
	@Column("postal_code")
	private String postalCode;
	
	@Column("supplier_id")
	private String supplierId;
	
	@Column("creation_date")
	private LocalDateTime creationDate;
	
	@Column("update_date")
	private LocalDateTime updateDate;
	
	@PersistenceConstructor
	public AddressEntity(Integer id, String address, String city, String country, String postalCode, String supplierId, 
			LocalDateTime creationDate, LocalDateTime updateDate) {
		
		this.id = id;
		this.address = address;
		this.city = city;
		this.country = country;
		this.postalCode = postalCode;
		this.supplierId = supplierId;
		this.creationDate = creationDate;
		this.updateDate = updateDate;
	}
	
	public AddressEntity withId(Integer id) {
		return new AddressEntity(id, address, city, country, postalCode, supplierId, creationDate, updateDate);
	}
}
