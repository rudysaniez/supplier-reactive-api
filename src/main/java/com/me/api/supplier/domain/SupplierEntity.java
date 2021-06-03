package com.me.api.supplier.domain;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringExclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Embedded.Nullable;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.EqualsAndHashCode.Exclude;
import lombok.NoArgsConstructor;

@Table("supplier")
@NoArgsConstructor
@Data
public class SupplierEntity {

	@Id @Column("id") @Exclude @ToStringExclude
	private Integer id;
	
	@Column("supplier_id") @NotBlank
	private String supplierId;
	
	@Column("name") @NotBlank @Exclude
	private String name;
	
	@Column("fiscal_country_code") @NotBlank @Exclude
	private String fiscalCountryCode;
	
	@Column("fiscal_id") @NotBlank @Exclude
	private String fiscalId;
	
	@Column("creation_date") @NotNull @Exclude
	private LocalDateTime creationDate;
	
	@Column("update_date") @Nullable @Exclude
	private LocalDateTime updateDate;
	
	public static final String SUPPLIER_ID = "SUP_%s_%s_%s";
	
	@PersistenceConstructor
	public SupplierEntity(Integer id, String supplierId, String name, String fiscalCountryCode, String fiscalId, LocalDateTime creationDate) {
		
		this.id = id;
		this.supplierId = supplierId;
		this.name = name.toUpperCase();
		this.fiscalCountryCode = fiscalCountryCode;
		this.fiscalId = fiscalId;
		this.creationDate = creationDate;
	}
	
	/**
	 * @param id
	 * @return {@link SupplierEntity}
	 */
	public SupplierEntity withId(Integer id) {
		return new SupplierEntity(id, supplierId, name, fiscalCountryCode, fiscalId, creationDate);
	}
}
