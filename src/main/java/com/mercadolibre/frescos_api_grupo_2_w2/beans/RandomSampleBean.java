package com.mercadolibre.frescos_api_grupo_2_w2.beans;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.SampleDTO;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class RandomSampleBean {
	private Random random = new Random();

	public SampleDTO random() {
		return new SampleDTO(random.nextInt(Integer.MAX_VALUE));
	}
}

