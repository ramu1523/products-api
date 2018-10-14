package com.tgt.myretail.products.api.dao;

import com.tgt.myretail.products.api.exception.AppException;
import com.tgt.myretail.products.api.exception.RedSkyAPIException;
import com.tgt.myretail.products.api.model.redskyresponse.RedskyProductDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class RedskyProductDAO {
	
	private final Logger LOGGER = LoggerFactory.getLogger(RedskyProductDAO.class);
	
	
	@Value("${redSkyAPI.url}")
	private String redskyAPIEndpoint;
	
	public RestTemplate getRestTemplate() {
		if (restTemplate == null) {
			restTemplate = new RestTemplate();
		}
		return restTemplate;
	}
	
	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	private RestTemplate restTemplate;
	
	@Retryable(value = RedSkyAPIException.class, backoff = @Backoff(delay = 1000, multiplier = 2))
	public RedskyProductDetail getProductDetails(Long id) {
		RedskyProductDetail redskyProduct;
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		try {
			redskyProduct = getRestTemplate().getForObject(redskyAPIEndpoint, RedskyProductDetail.class, id);
		} catch(HttpClientErrorException.NotFound ex) {
			throw  new AppException(ex.getMessage());
		}catch(Exception ex1){
			throw new RedSkyAPIException("Redsky API Error: "+ ex1.getMessage());
		}
		return redskyProduct;
	}
}
