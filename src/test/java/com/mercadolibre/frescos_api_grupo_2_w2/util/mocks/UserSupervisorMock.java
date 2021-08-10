package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.SellerDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.dtos.SupervisorDTO;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Seller;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

public class UserSupervisorMock {
    public static Supervisor validSupervisor (Optional<Long> id) {
        Supervisor supervisorMock = new Supervisor();
        supervisorMock.setEmail("any_email@email.com");
        supervisorMock.setPassword("any_password");
        supervisorMock.setRole("SUPERVISOR");

        if (id != null) {
            supervisorMock.setUserId(id.get());
        }
        return supervisorMock;
    }

    public static SupervisorDTO validSupervisorDTO () {
        Supervisor sellerMock = validSupervisor(null);
        SupervisorDTO supervisorDTO = new SupervisorDTO();
        BeanUtils.copyProperties(sellerMock, supervisorDTO, "userId");
        return supervisorDTO;
    }
}
