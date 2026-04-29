package com.example.appointment.services;


import com.example.appointment.dtos.ProfessionalRequestDTO;
import com.example.appointment.dtos.ProfessionalResponseDTO;
import com.example.appointment.dtos.ProfessionalUpdateDTO;
import com.example.appointment.exceptions.NotFoundExceptionT;
import com.example.appointment.models.Professional;
import com.example.appointment.repositories.ProfessionalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfessionalService {

    private final ProfessionalRepository repository;


    // CREATE PROFESSIONAL
    public ProfessionalResponseDTO createProfessional(ProfessionalRequestDTO requestDTO) {

        Professional professional = new Professional(
                requestDTO.name(),
                requestDTO.bio()
        );
        repository.save(professional);

        return ProfessionalResponseDTO.fromModel(professional);
    }

    // FIND ALL PROFESSIONAL
    public List<ProfessionalResponseDTO> findAllProfessionals() {
        return repository.findAll()
                .stream()
                .map(ProfessionalResponseDTO::fromModel)
                .toList();
    }

    //FIND BY ID PROFESSIONAL
    public ProfessionalResponseDTO findProfessionalById(UUID id) {
      Professional professional = repository.findById(id).orElseThrow(() -> new NotFoundExceptionT("ID não encontrado" +
              "tente novamente com ID válido."));

      return ProfessionalResponseDTO.fromModel(professional);
    }

    //Update PRofessional by ID
    public ProfessionalResponseDTO updateProfessional(UUID id, ProfessionalUpdateDTO updateDTO) {
        Professional professionalToUpdate = repository.findById(id).orElseThrow(() -> new NotFoundExceptionT("ID não encontrado" +
                "tente novamente com ID válido."));

        updateDTO.name().ifPresent(professionalToUpdate::setName);
        updateDTO.bio().ifPresent(professionalToUpdate::setBio);
        repository.save(professionalToUpdate);

        return ProfessionalResponseDTO.fromModel(professionalToUpdate);
    }

    //Delete PRofessional
    public void deleteProfessional(UUID id) {
        Professional professional = repository.findById(id).orElseThrow(() -> new NotFoundExceptionT("ID não encontrado" +
                "tente novamente com ID válido."));
        repository.delete(professional);
    }
}
