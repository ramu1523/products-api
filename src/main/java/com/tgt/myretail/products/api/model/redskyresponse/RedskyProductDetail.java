package com.tgt.myretail.products.api.model.redskyresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RedskyProductDetail {
	
	@JsonProperty("product")
	RedskyProduct product;
}
