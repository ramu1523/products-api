package com.tgt.myretail.products.api.controller;

import com.tgt.myretail.products.api.model.Product;
import com.tgt.myretail.products.api.service.ProductService;
import javafx.application.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductsControllerTest {
	
	private Logger LOGGER = LoggerFactory.getLogger(ProductsControllerTest.class);
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	private ProductService productService;
	
	@Test
	public void testProductAPIResponse() throws Exception
	{
		//when(productService.getProductData(anyDouble())).thenReturn(new Product());
		mockMvc.perform(get("/myretail/v1/products/1")).andExpect(status().is(HttpStatus.OK.value()));
	 
	}
	
	@Test
	public void testGetProductDataShouldCallServiceOnce() throws Exception
	{
		//It should call service once
		mockMvc.perform(get("/myretail/v1/products/1")).andExpect(status().is(HttpStatus.OK.value()));
		verify(productService, times(1)).getProductData(new Long(1));
	}
	
	@Test
	public void testGetProductDataWithInvalidRequest() throws Exception
	{
		//test with Invalid product id - test
		mockMvc.perform(get("/myretail/v1/products/test")).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
		
	}
	
	@Test
	public void testUpdateProductPriceWithInvalidPrice() throws Exception
	{
		//test with Invalid product id - test
		String requestObject = "{\n    \"id\": 13860428,\n    \"name\": \"The Big Lebowski (Blu-ray)\",\n    \"current_price\": {\n        \"value\": 0.00,\n        \"currency_code\": \"USD\"\n    }\n}";
		mockMvc.perform(put("/myretail/v1/products").contentType(MediaType.APPLICATION_JSON).content(requestObject)).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
		
	}
	
	@Test
	public void testUpdateProductPriceWithInvalidCurrencyCode() throws Exception
	{
		//test with Invalid product id - test
		String requestObject = "{\n    \"id\": 13860428,\n    \"name\": \"The Big Lebowski (Blu-ray)\",\n    \"current_price\": {\n        \"value\": 1.00,\n        \"currency_code\": \"XMR\"\n    }\n}";
		mockMvc.perform(put("/myretail/v1/products").contentType(MediaType.APPLICATION_JSON).content(requestObject)).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
	}
	
	@Test
	public void testUpdateProductPriceWithInvalidProductId() throws Exception
	{
		//test with Invalid product id - test
		String requestObject = "{\n    \"id\": \"TEST\",\n    \"name\": \"The Big Lebowski (Blu-ray)\",\n    \"current_price\": {\n        \"value\": 1.00,\n        \"currency_code\": \"USD\"\n    }\n}";
		mockMvc.perform(put("/myretail/v1/products").contentType(MediaType.APPLICATION_JSON).content(requestObject)).andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
	}
	
	@Test
	public void testUpdateProductPriceWithValidInput() throws Exception
	{
		//test with Invalid product id - test
		String requestObject = "{\n    \"id\": \"13860428\",\n    \"name\": \"The Big Lebowski (Blu-ray)\",\n    \"current_price\": {\n        \"value\": 1.00,\n        \"currency_code\": \"USD\"\n    }\n}";
		mockMvc.perform(put("/myretail/v1/products").contentType(MediaType.APPLICATION_JSON).content(requestObject)).andExpect(status().is(HttpStatus.OK.value()));
	}
}
