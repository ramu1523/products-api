package com.tgt.myretail.products.api.dao;

import com.tgt.myretail.products.api.model.redskyresponse.RedskyProductDetail;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedskyProductDAO.class}, properties = {"redSkyAPI.url=https://redsky.target.com/v2/pdp/tcin/{id}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics"})
public class RedskyProductDAOTest {
	
	RedskyProductDAO redskyProductDAO;
	
	@Autowired
	RedskyProductDAO productDAO;
	
	//String redSkyAPIURL = "https://redsky.target.com/v2/pdp/tcin/{id}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";
	@Before
	public void setUp()
	{
		RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
		//redskyProductDAO = new RedskyProductDAO(restTemplateBuilder);
	}
	
	@Ignore
	public void testRedSkyApiClientWithInvalidProduct() {
		int expCount =0;
		try {
		//	ReflectionTestUtils.setField(redskyProductDAO,"redSkyAPI.url", redSkyAPIURL);
			RedskyProductDetail redskyProductDetail = redskyProductDAO.getProductDetails(new Long(15117729));
		}catch(HttpClientErrorException.NotFound ex)
		{
			expCount ++;
		}
		Assert.assertTrue(expCount == 1);
	}
	
	@Ignore
	public void testRedSkyApiClientWithValidProduct() {
	//	ReflectionTestUtils.setField(redskyProductDAO, "redSkyAPI.url", redSkyAPIURL);
		RedskyProductDetail redskyProductDetail = redskyProductDAO.getProductDetails(new Long(13860428));
		Assert.assertNotNull(redskyProductDetail.getProduct().getItem().getProductDescription().getTitle());
	}
}
