package com.tgt.myretail.products.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;

@Document(collection = "product")
@Data
public class Product {
	
	@NotNull
	@Field("id")
	@Indexed(name="id", unique = true)
	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("name")
	private String productName;
	
	@JsonProperty("current_price")
	private Price currentPrice;
	
}
