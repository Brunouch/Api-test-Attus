package com.test.Attus.service;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.test.Attus.controller.EnderecoController;
import com.test.Attus.dataVOV1.EnderecoVOV1;
import com.test.Attus.exceptions.RequiredObjectIsNullException;
import com.test.Attus.exceptions.ResourceNotFoundException;
import com.test.Attus.mapper.MapperDozzer;
import com.test.Attus.models.Endereco;
import com.test.Attus.repository.EnderecoRepository;

@Service
public class EnderecoService {
    
    private Logger logger = Logger.getLogger(EnderecoService.class.getName());

	@Autowired
	EnderecoRepository repository;

    public List<EnderecoVOV1> findAll() {

		logger.info("listando todas as pessoas!");

		var enderecos = MapperDozzer.parseListObjects(repository.findAll(), EnderecoVOV1.class);
		enderecos.stream()
				.forEach(p -> p.add(linkTo(methodOn(EnderecoController.class).findById(p.getKey())).withSelfRel()));
		return enderecos;
	}

    public EnderecoVOV1 findById(Long id) {

		logger.info("procurando uma pessoa!");

		var entity = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("ID não encontrado"));

		var vo = MapperDozzer.parseObject(entity, EnderecoVOV1.class);
		vo.add(linkTo(methodOn(EnderecoController.class).findById(id)).withSelfRel());
		return vo;
	}

    public EnderecoVOV1 create(EnderecoVOV1 endereco) {

		if (endereco == null)
			throw new RequiredObjectIsNullException();
		logger.info("Criando uma pessoa!");

		var entity = MapperDozzer.parseObject(endereco, Endereco.class);
		var vo = MapperDozzer.parseObject(repository.save(entity), EnderecoVOV1.class);
		vo.add(linkTo(methodOn(EnderecoController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

    public EnderecoVOV1 update(EnderecoVOV1 endereco) {

		if (endereco == null)
			throw new RequiredObjectIsNullException();
		logger.info("Atualizando uma pessoa");

		var entity = repository.findById(endereco.getKey())
				.orElseThrow(() -> new ResourceNotFoundException("ID não encontrado"));

        entity.setLogradouro(endereco.getLogradouro());
        entity.setPessoa(endereco.getPessoa());
		entity.setCep(endereco.getCep());
        entity.setNumero(endereco.getNumero());
		entity.setCidade(endereco.getCidade());
		entity.setEstado(endereco.getEstado());
        entity.setEnderecoPrincipal(endereco.getEnderecoPrincipal());


		var vo = MapperDozzer.parseObject(repository.save(entity), EnderecoVOV1.class);
		vo.add(linkTo(methodOn(EnderecoController.class).findById(vo.getKey())).withSelfRel());
		return vo;
	}

    public void delete(Long id) {

		logger.info("Deletando um endereco!");

		var entity = repository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("ID não encontrado!"));
		repository.delete(entity);
	}
}
