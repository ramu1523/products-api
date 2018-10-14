package com.tgt.myretail.products.api.dao;

import com.mongodb.client.result.UpdateResult;
import com.tgt.myretail.products.api.exception.AppException;
import com.tgt.myretail.products.api.model.CurrencyCode;
import com.tgt.myretail.products.api.model.Price;
import com.tgt.myretail.products.api.model.Product;
import org.bson.BsonValue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProductsDAOTest {
	
	Logger LOGGER = LoggerFactory.getLogger(ProductsDAOTest.class);
	MongoTemplate mongoTemplate = mock(MongoTemplate.class);
	
	private ProductDAO  productDAO;
	
	@Before
	public void setUp()
	{
		  productDAO = new ProductDAO(mongoTemplate);
	}
	
	@Test
	public void testGetProductDataWithInvalidProductId()
	{
		when(mongoTemplate.findOne(any(),any(),any())).thenReturn(null);
		Product product = null;
		try {
			product = productDAO.getProductData(new Long(15117729));
		}catch(Exception exp)
		{
			Assert.assertTrue(exp instanceof AppException);
			Assert.assertEquals("Mongo DB Error: 404 NOT_FOUND", exp.getMessage());
		}
		Assert.assertTrue(null == product);
	}
	
	@Test
	public void testGetProductDataWithValidProductId()
	{
		when(mongoTemplate.findOne(any(),any(),any())).thenReturn(mockProductDetailsFromMongo());
		Product product = productDAO.getProductData(new Long(13860428));
		Assert.assertEquals(new Long(13860428), product.getId());
	}
	
	@Test
	public void testGetProductDataWithAppException()
	{
		when(mongoTemplate.findOne(any(),any(),any())).thenThrow(new AppException("connection error"));
		try {
			productDAO.getProductData(new Long(13860428));
		}catch(Exception exp)
		{
			LOGGER.error(exp.getMessage());
		}
		//verify(mongoTemplate.findOne(any(),any(),any()), times(3));
	}
	
	@Test
	public void testUpdateProductPriceWithDBReturningNull() {
		Product product = mockProductDetailsFromMongo();
		Mockito.when(mongoTemplate.updateFirst(any(),any(),eq(Product.class))).thenReturn(null);
		Product expected = null;
		int expCount = 0;
		try {
			expected = productDAO.updateProductPrice(product);
		}catch(Exception ex)
		{
			expCount ++;
		}
		Assert.assertTrue(1 == expCount);
		Assert.assertEquals(null, expected);
	}
	
	private UpdateResult mockUpdateResultCount() {
		UpdateResult updateResult = new UpdateResult() {
			@Override
			public boolean wasAcknowledged() {
				return false;
			}
			
			@Override
			public long getMatchedCount() {
				return 1;
			}
			
			@Override
			public boolean isModifiedCountAvailable() {
				return false;
			}
			
			@Override
			public long getModifiedCount() {
				return 1;
			}
			
			@Override
			public BsonValue getUpsertedId() {
				return null;
			}
		};
		return updateResult;
	}
	
	@Test
	public void testUpdateProductPriceWithExistingProduct() {
		
		Product product = mockProductDetailsFromMongo();
		Mockito.when(mongoTemplate.updateFirst(any(),any(),eq(Product.class))).thenReturn(mockUpdateResultCount());
		when(mongoTemplate.findOne(any(),any(),any())).thenReturn(product);
		Product actual = productDAO.updateProductPrice(product);
		Assert.assertEquals(product, actual);
	}
	
	
	private Product mockProductDetailsFromMongo() {
		Product product = new Product();
		Price price = new Price();
		price.setPrice(60.06);
		price.setCurrencyCode(CurrencyCode.USD);
		product.setId(new Long(13860428));
		product.setCurrentPrice(price);
		return product;
	}

}
