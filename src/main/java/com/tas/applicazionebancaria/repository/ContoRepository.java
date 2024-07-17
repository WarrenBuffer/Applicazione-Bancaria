package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.Conto;

@Repository("ContoRepository")
public interface ContoRepository extends JpaRepository<Conto, Long>{

}
