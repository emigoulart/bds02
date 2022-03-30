package com.devsuperior.bds02.services;

import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.services.exceptions.DatabaseException;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

    @Autowired
    private CityRepository repository;

    @Transactional(readOnly = true)
    public List<CityDTO> findAll() {
        return repository.findAll(Sort.by("name")).stream()
                .map(CityDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CityDTO insert(CityDTO cityDTO) {
        City entity = new City();
        entity.setName(cityDTO.getName());
        entity = repository.save(entity);
        return new CityDTO(entity);
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException emptyResultDaoException) {
            throw new ResourceNotFoundException("Id not found: " + id);
        }catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new DatabaseException("Integrity violation");
        }
    }
}
