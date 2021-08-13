package com.mercadolibre.frescos_api_grupo_2_w2.util.mocks;

import com.mercadolibre.frescos_api_grupo_2_w2.dtos.forms.user.SupervisorForm;
import com.mercadolibre.frescos_api_grupo_2_w2.entities.Supervisor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserSupervisorMock {
    public static Long userId = 1L;
    private static PasswordEncoder encoder = new BCryptPasswordEncoder();

    public static Supervisor validSupervisor (Long id) {
        Supervisor supervisorMock = new Supervisor();
        supervisorMock.setEmail("admin@email.com");
        supervisorMock.setPassword(encoder.encode("123"));
        supervisorMock.setRole("SUPERVISOR");
        supervisorMock.setWarehouse(null);

        if (id != null) {
            supervisorMock.setUserId(id);
        }
        return supervisorMock;
    }

    public static Supervisor validSupervisor () {
        Supervisor supervisorMock = new Supervisor();
        supervisorMock.setEmail("admin@email.com");
        supervisorMock.setPassword(encoder.encode("123"));
        supervisorMock.setRole("SUPERVISOR");
        supervisorMock.setUserId(userId);
        return supervisorMock;
    }

    public static SupervisorForm validSupervisorDTO () {
        Supervisor sellerMock = validSupervisor(null);
        SupervisorForm supervisorDTO = new SupervisorForm();
        BeanUtils.copyProperties(sellerMock, supervisorDTO, "userId");
        return supervisorDTO;
    }
}
