package com.tgt.myretail.products.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.*;
import java.math.BigDecimal;

@Data
public class Price {
	
	
	@Field("value")
	@JsonProperty("value")
	//@DecimalMin("9999999.99")
	@DecimalMin("0.01")
	private Double price;
	
	@NotNull
	@Field("currency_code")
	@JsonProperty("currency_code")
	private CurrencyCode currencyCode;
}
