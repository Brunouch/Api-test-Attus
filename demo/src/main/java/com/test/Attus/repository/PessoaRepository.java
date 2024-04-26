package com.test.Attus.repository;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.Attus.models.Pessoa;

@ComponentScan
@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {}
