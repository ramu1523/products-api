package com.tgt.myretail.products.api.service;

import com.tgt.myretail.products.api.dao.ProductDAO;
import com.tgt.myretail.products.api.dao.RedskyProductDAO;
import com.tgt.myretail.products.api.exception.AppException;
import com.tgt.myretail.products.api.model.Product;
import com.tgt.myretail.products.api.model.redskyresponse.RedskyProduct;
import com.tgt.myretail.products.api.model.redskyresponse.RedskyProductDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ProductService {
	
	@Autowired
	ProductDAO productDAO;
	
	@Autowired
	RedskyProductDAO redskyProductDAO;
	

	public Product getProductData(Long id) throws Exception
	{
		CompletableFuture<RedskyProductDetail> redskyProductDetailFuture;
		CompletableFuture<Product> productCompletableFuture;
		redskyProductDetailFuture = CompletableFuture.supplyAsync(() -> redskyProductDAO.getProductDetails(id));
		productCompletableFuture = CompletableFuture.supplyAsync(() -> productDAO.getProductData(id));
		
		CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(redskyProductDetailFuture, productCompletableFuture);
		combinedFuture.get();
		RedskyProductDetail redskyProduct = redskyProductDetailFuture.get();
		Product product =  productCompletableFuture.get();
		if(product == null)
		{
			throw new AppException("Product not found");
		}
		product.setProductName(redskyProduct.getProduct().getItem().getProductDescription().getTitle());
		return  product;
	}
	
	public Product updateProductPrice(Product product)
	{
		Product updatedProduct =  productDAO.updateProductPrice(product);
		if(updatedProduct == null)
		{
			throw new AppException("Product not found");
		}
		return updatedProduct;
	}
	
}
