package com.me.api.supplier.bo;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;
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

@Table("supplier") @NoArgsConstructor
@Data
public class SupplierEntity implements Serializable {

	@Id @Column("id") 
	@Exclude @ToStringExclude
	private Integer id;
	
	@Column("supplierId")
	@NotEmpty @NotNull
	private String supplierId;
	
	@Column("name") 
	@NotEmpty @NotNull
	private String name;
	
	@Column("shortName")
	@Nullable @Exclude
	private String shortName;
	
	@Column("otherName")
	@Nullable @Exclude
	private String otherName;
	
	@Column("secondSupplierOtherName")
	@Nullable @Exclude
	private String secondSupplierOtherName;
	
	@Column("fiscalCountryCode")
	@NotEmpty @NotNull
	private String fiscalCountryCode;
	
	@Column("fiscalId")
	@NotEmpty @NotNull
	private String fiscalId;
	
	@Column("legalId")
	@Nullable @Exclude
	private String legalId;
	
	@Column("amountOfCapital")
	@Nullable @Exclude
	private Long amountOfCapital;
	
	@Column("supplierType")
	@Exclude @NotEmpty @NotNull
	private String supplierType;
	
	@Column("creationDate")
	@Nullable @Exclude
	private LocalDateTime creationDate;
	
	@Column("updateDate")
	@Nullable @Exclude
	private LocalDateTime updateDate;
	
	public static final String SUPPLIER_ID = "SUP_%s_%s_%s";
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceConstructor
	public SupplierEntity(Integer id, String supplierId, String name, String fiscalCountryCode, String fiscalId, String supplierType) {
		
		this.id = id;
		this.supplierId = supplierId;
		this.name = name.toUpperCase();
		this.fiscalCountryCode = fiscalCountryCode;
		this.fiscalId = fiscalId;
		this.supplierType = supplierType.toUpperCase();
		this.creationDate = LocalDateTime.now();
	}
	
	/**
	 * @param id
	 * @return {@link SupplierEntity}
	 */
	public SupplierEntity withId(Integer id) {
		return new SupplierEntity(id, supplierId, name, fiscalCountryCode, fiscalId, supplierType);
	}
	
	public static enum Type {
		
		SUPPLIER, 
		SUB_SUPPLIER, 
		SUPER_SUPPLIER;
	}
}
