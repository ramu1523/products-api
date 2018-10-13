package com.tgt.myretail.products.api.service;

import com.tgt.myretail.products.api.dao.ProductDAO;
import com.tgt.myretail.products.api.dao.RedskyProductDAO;
import com.tgt.myretail.products.api.exception.AppException;
import com.tgt.myretail.products.api.model.CurrencyCode;
import com.tgt.myretail.products.api.model.Price;
import com.tgt.myretail.products.api.model.Product;
import com.tgt.myretail.products.api.model.redskyresponse.Item;
import com.tgt.myretail.products.api.model.redskyresponse.ProductDescription;
import com.tgt.myretail.products.api.model.redskyresponse.RedskyProduct;
import com.tgt.myretail.products.api.model.redskyresponse.RedskyProductDetail;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class ProductsServiceTest {
private Logger LOGGER = LoggerFactory.getLogger(ProductsServiceTest.class);

@InjectMocks
private  ProductService productService;

@Mock
private  RedskyProductDAO redskyProductDAO;

@Mock
private ProductDAO productDAO;

@Before
public void setUp()
{
	MockitoAnnotations.initMocks(this);
}
	
	@Test
	public void testGetProductDetailsWithValidInput() throws Exception{
		
		RedskyProductDetail redskyProductDetail = mockRedskyAPIResponse();
		
		Product product = mockProductDetailsFromMongo();
		
		Mockito.when(redskyProductDAO.getProductDetails(new Long(13860428))).thenReturn(redskyProductDetail);
		
		Mockito.when(productDAO.getProductData(new Long(13860428))).thenReturn(product);
		
		product.setProductName("The Big Lebowski (Blu-ray)");
		
		Product expected = product;
		
		Assert.assertEquals(expected, productService.getProductData(new Long(13860428)));
	}
	
	
	@Test
	public void testGetProductDetailsWithInvalidProductIdInRedsky() throws Exception{
		
		Product product = mockProductDetailsFromMongo();
		
		Mockito.when(redskyProductDAO.getProductDetails(new Long(1386042812))).thenThrow(new AppException("404 Not Found"));
		
	//	ProductPrice productPrice = new ProductPrice("13860428", new BigDecimal(13.75), Currency.USD);
		Mockito.when(productDAO.getProductData(new Long(1386042812))).thenReturn(product);
		int exceptionCount =0 ;
		try
		{
			productService.getProductData(new Long(1386042812));
		}catch(Exception ex)
		{
			exceptionCount = 1;
			LOGGER.error(ex.getMessage());
			Assert.assertEquals("com.tgt.myretail.products.api.exception.AppException: 404 Not Found", ex.getMessage());
		}
		Assert.assertTrue(exceptionCount == 1);
		
	}
	
	@Test
	public void testGetProductDetailsWithRedSkyReturningObjectAndDbReturningNull() throws Exception{
		
		
		RedskyProductDetail redskyProductDetail = mockRedskyAPIResponse();
		
		Mockito.when(redskyProductDAO.getProductDetails(new Long(13860428))).thenReturn(redskyProductDetail);
		
		Mockito.when(productDAO.getProductData(new Long(13860428))).thenReturn(null);
		int exceptionCount = 0;
		try {
			productService.getProductData(new Long(13860428));
		}catch(AppException ex)
		{
			Assert.assertEquals("Product not found" , ex.getMessage());
		    exceptionCount ++;
		}
		Assert.assertEquals(1, exceptionCount);
	}
	
	@Test
	public void testUpdateProductPriceWithDBReturningNull() {
		
		Product product = mockProductDetailsFromMongo();
		Mockito.when(productDAO.updateProductPrice(any())).thenReturn(null);
		int expCount = 0;
		try {
			productService.updateProductPrice(product);
		}catch(Exception ex)
		{
			expCount ++;
			Assert.assertEquals("Product not found", ex.getMessage());
		}
		Assert.assertTrue(1 == expCount);
	}
	
	@Test
	public void testUpdateProductPriceWithExistingProduct() {
		
		Product product = mockProductDetailsFromMongo();
		Mockito.when(productDAO.updateProductPrice(product)).thenReturn(product);
		Product actual = productService.updateProductPrice(product);
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
	
	private RedskyProductDetail mockRedskyAPIResponse() {
		RedskyProductDetail redskyProductDetail = new RedskyProductDetail();
		RedskyProduct redskyProduct = new RedskyProduct();
		Item item = new Item();
		ProductDescription productDescription = new ProductDescription();
		productDescription.setTitle("The Big Lebowski (Blu-ray)");
		item.setProductDescription(productDescription);
		redskyProduct.setItem(item);
		redskyProductDetail.setProduct(redskyProduct);
		return redskyProductDetail;
	}
	
	
}
