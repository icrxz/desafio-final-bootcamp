package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SupervisorForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Warehouse;
import org.springframework.beans.BeanUtils;

import java.util.Optional;

public class UserSupervisorMock {
    public static Supervisor validSupervisor (Optional<Long> id) {
        Supervisor supervisorMock = new Supervisor();
        supervisorMock.setEmail("any_email@email.com");
        supervisorMock.setPassword("any_password");
        supervisorMock.setRole("SUPERVISOR");
        supervisorMock.setWarehouse(null);

        if (id != null) {
            supervisorMock.setUserId(id.get());
        }
        return supervisorMock;
    }

    public static Supervisor validSupervisor () {
        Supervisor supervisorMock = new Supervisor();
        supervisorMock.setEmail("any_email@email.com");
        supervisorMock.setPassword("any_password");
        supervisorMock.setRole("SUPERVISOR");
        supervisorMock.setUserId(1L);
        return supervisorMock;
    }

    public static SupervisorForm validSupervisorDTO () {
        Supervisor sellerMock = validSupervisor(null);
        SupervisorForm supervisorDTO = new SupervisorForm();
        BeanUtils.copyProperties(sellerMock, supervisorDTO, "userId");
        return supervisorDTO;
    }
}
