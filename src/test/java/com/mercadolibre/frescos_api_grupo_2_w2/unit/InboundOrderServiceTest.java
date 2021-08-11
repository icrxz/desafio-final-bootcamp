package com.mercadolibre.frescos_api_grupo_2_w2.unit;

import com.mercadolibre.frescos_api_grupo_2_w2.repositories.InboundOrderRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.repositories.SupervisorRepository;
import com.mercadolibre.frescos_api_grupo_2_w2.services.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class InboundOrderServiceTest {
    @InjectMocks
    private InboundOrderService inboundOrderService;

    @Mock
    private InboundOrderRepository inboundOrderRepository;

    @Mock
    private BatchService batchService;

    @Mock
    private WarehouseService warehouseService;

    @Mock
    private SectionService sectionService;

    @BeforeEach
    public void setUp() throws Exception {
        inboundOrderRepository.deleteAll();
        InboundOrderService inboundOrderService = new InboundOrderService(inboundOrderRepository, batchService, warehouseService, sectionService);
    }
}
