package com.mercadolibre.frescos_api_grupo_2_w2.unit.beans;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.SampleDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.beans.RandomSampleBean;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RandomSampleBeanTest {

	@Test
	public void randomPositiveTestOK() {
		RandomSampleBean randomSample = new RandomSampleBean();

		SampleDTO sample = randomSample.random();
		
		assertTrue(sample.getRandom() >= 0);
	}
}
