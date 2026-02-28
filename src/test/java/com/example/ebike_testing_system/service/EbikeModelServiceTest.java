package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.exception.NotFoundException;
import com.example.ebike_testing_system.model.EbikeModel;
import com.example.ebike_testing_system.dto.EbikeModelProjection;
import com.example.ebike_testing_system.repository.EbikeModelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
//@ActiveProfiles("test")
//@SpringBootTest
class EbikeModelServiceTest {

    private EbikeModelRepository repo;
    private EbikeModelService svc;

    @BeforeEach
    void setUp() {
        repo = mock(EbikeModelRepository.class);
        svc  = new EbikeModelService(repo);
    }

    @Test
    void findAll_returnsRepoList() {
        EbikeModel m = new EbikeModel();
        m.setName("X");
        List<EbikeModel> data = List.of(m);

        given(repo.findAll()).willReturn(data);

        assertEquals(data, svc.findAll());
    }

    @Test
    void findAllProjected_returnsRepoProjections() {
        // use a Mockito mock for the projection interface
        EbikeModelProjection p = mock(EbikeModelProjection.class);
        List<EbikeModelProjection> proj = List.of(p);

        given(repo.findAllProjected()).willReturn(proj);

        assertEquals(proj, svc.findAllProjected());
    }

    @Test
    void deleteModel_missingId_throwsNotFound() {
        given(repo.findById(42)).willReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> svc.deleteModel(42));
    }

    @Test
    void deleteModel_integrityViolation_throwsIllegalState() {
        EbikeModel m = new EbikeModel();
        m.setId(1);

        given(repo.findById(1)).willReturn(Optional.of(m));
        willThrow(new org.springframework.dao.DataIntegrityViolationException("fk"))
                .given(repo).deleteById(1);

        IllegalStateException ex =
                assertThrows(IllegalStateException.class,
                        () -> svc.deleteModel(1));

        assertTrue(ex.getMessage().contains("referenced"));
    }
}
