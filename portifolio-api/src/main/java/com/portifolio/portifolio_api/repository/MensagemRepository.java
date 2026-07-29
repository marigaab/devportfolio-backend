package com.portifolio.portifolio_api.repository;

import com.portifolio.portifolio_api.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;

 //@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {
}