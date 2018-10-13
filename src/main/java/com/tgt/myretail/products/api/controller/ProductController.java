package com.tgt.myretail.products.api.controller;

import com.tgt.myretail.products.api.model.Product;
import com.tgt.myretail.products.api.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/myretail/v1/")
public class ProductController {
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	public ProductService getProductService() {
		return productService;
	}
	
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	
	@Autowired
	ProductService productService;
	
	@RequestMapping(value="products/{product_id}", method = RequestMethod.GET)
	public ResponseEntity<Object> getProductData(@PathVariable("product_id") Long productId) throws Exception
	{
		LOGGER.info("product id:"+productId);
		Product productResponse = productService.getProductData(productId);
		return ResponseEntity.ok(productResponse);
	}
	
	@RequestMapping(value="products", method = RequestMethod.PUT)
	public ResponseEntity<Object> updateProductPrice(@RequestBody Product product) {
		Product productResponse = null;
		if(isValidProductPrice(product.getCurrentPrice().getPrice())) {
			 productResponse = productService.updateProductPrice(product);
		}else
		{
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\" : \"Invalid Product Price: Price should be between 0.1 and 999999.99\"}");
		}
		return ResponseEntity.ok(productResponse);
	}
	
	protected boolean isValidProductPrice(double price)
	{
		if(price < 0.1 || price > 999999.99)
		{
			return false;
		}
		return true;
	}
	
	}
