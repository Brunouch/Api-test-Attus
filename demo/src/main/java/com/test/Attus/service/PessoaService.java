package com.test.Attus.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Service;

import com.test.Attus.controller.PessoaController;
import com.test.Attus.dataVOV1.PessoaVOV1;
import com.test.Attus.exceptions.RequiredObjectIsNullException;
import com.test.Attus.exceptions.ResourceNotFoundException;
import com.test.Attus.mapper.MapperDozzer;
import com.test.Attus.models.Pessoa;
import com.test.Attus.repository.PessoaRepository;


@Service
public class PessoaService {

    private Logger logger = Logger.getLogger(PessoaService.class.getName());

	@Autowired
	PessoaRepository repository;

    public List<PessoaVOV1> findAll() {

		logger.info("Finding all people!");

		var persons = MapperDozzer.parseListObjects(repository.findAll(), PessoaVOV1.class);
		persons.stream()
				.forEach(p -> p.add(linkTo(methodOn(PessoaController.class).findById(p.getKey())).withSelfRel()));
		return persons;
	}

    public PessoaVOV1 findById(Long id) {

		logger.info("Finding one person!");

		var entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

		var vo = MapperDozzer.parseObject(entity, PessoaVOV1.class);
		vo.add(linkTo(methodOn(PessoaController.class).findById(id)).withSelfRel());
		return vo;
	}

    public PessoaVOV1 create(PessoaVOV1 person) {

		if (person == null)
			throw new RequiredObjectIsNullException();
		logger.info("Creating one person!");

		var entity = MapperDozzer.parseObject(person, Pessoa.class);
		var vo = MapperDozzer.parseObject(repository.save(entity), PessoaVOV1.class);
		vo.add(linkTo(methodOn(PessoaController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

    public PessoaVOV1 update(PessoaVOV1 person) {

		if (person == null)
			throw new RequiredObjectIsNullException();
		logger.info("Updating one person!");

		var entity = repository.findById(person.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setNomeCompleto(person.getNomeCompleto());
		entity.setDataNascimento(person.getDataNascimento());

		var vo = MapperDozzer.parseObject(repository.save(entity), PessoaVOV1.class);
		vo.add(linkTo(methodOn(PessoaController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

    public void delete(Long id) {

		logger.info("Deleting one person!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
		repository.delete(entity);
	}


    
}
