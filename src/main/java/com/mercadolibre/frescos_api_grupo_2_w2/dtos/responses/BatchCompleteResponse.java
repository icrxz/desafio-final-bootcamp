package com.mercadolibre.frescos_api_grupo_2_w2.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchCompleteResponse {

    private SectionResponse sectionResponses;
    private UUID productId;
    private List<BatchListResponse> batchStock = new ArrayList<>();
}
