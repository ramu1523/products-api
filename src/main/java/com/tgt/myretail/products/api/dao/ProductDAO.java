package com.tgt.myretail.products.api.dao;

import com.mongodb.client.result.UpdateResult;
import com.tgt.myretail.products.api.exception.AppException;
import com.tgt.myretail.products.api.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class ProductDAO {
	
	private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	MongoTemplate mongoTemplate;

	@Autowired
	ProductDAO(MongoTemplate mongoTemplate)
	{
		this.mongoTemplate = mongoTemplate;
	}
	
	/**
	 *	This
	 * @param id
	 * @return
	 */
	@Retryable(value = AppException.class, backoff = @Backoff(delay = 1000, multiplier = 2))
	public Product getProductData(Long id)
	{
		Product product;
		try {
			product = mongoTemplate.findOne(Query.query(Criteria.where("id").is(id)), Product.class, "product");
			LOGGER.info("ProductDAO --> inside getProductData method");
		}catch(Exception ex1){
			throw new AppException("Mongo DB Error: "+ ex1.getMessage());
		}
		return  product;
	}
	
	/**
	 *
	 * @param product
	 * @return
	 */
	@Retryable(value = AppException.class, backoff = @Backoff(delay = 1000, multiplier = 2))
	public Product updateProductPrice(Product product)
	{
		UpdateResult updateResult;
		try {
			 updateResult = mongoTemplate.updateFirst(Query.query(Criteria.where("id").is(product.getId())),
					//Update.update("tcinGroups.$.tcins",tcinGroup.getTcins()),UserTcinGroups.class);
			Update.update("currentPrice.value", product.getCurrentPrice().getPrice()), Product.class);
			LOGGER.info("ProductDAO --> inside updateProductPrice method");
		}catch(Exception ex1){
			throw new AppException("Mongo DB Error: "+ ex1.getMessage());
		}
		if(updateResult.getMatchedCount() == 1)
		{
			Product updatedProduct = getProductData(product.getId());
			updatedProduct.setProductName(product.getProductName());
			return updatedProduct;
		}
		return null;
	}

}
