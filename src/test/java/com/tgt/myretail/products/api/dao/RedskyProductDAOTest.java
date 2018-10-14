package com.tgt.myretail.products.api.dao;

import com.tgt.myretail.products.api.exception.AppException;
import com.tgt.myretail.products.api.model.redskyresponse.RedskyProductDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RedskyProductDAO.class}, properties = {"redSkyAPI.url=https://redsky.target.com/v2/pdp/tcin/{id}?excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics"})
public class RedskyProductDAOTest {
	
	
	@Autowired
	RedskyProductDAO redskyProductDAO;
	
	@Test
	public void testRedSkyApiClientWithInvalidProduct() {
		int expCount =0;
		try {
			RedskyProductDetail redskyProductDetail = redskyProductDAO.getProductDetails(new Long(15117729));
		}catch(AppException ex)
		{
			expCount ++;
		}
		Assert.assertTrue(expCount == 1);
	}
	
	@Test
	public void testRedSkyApiClientWithValidProduct() {
	//	ReflectionTestUtils.setField(redskyProductDAO, "redSkyAPI.url", redSkyAPIURL);
		RedskyProductDetail redskyProductDetail = redskyProductDAO.getProductDetails(new Long(13860428));
		Assert.assertNotNull(redskyProductDetail.getProduct().getItem().getProductDescription().getTitle());
	}
}
