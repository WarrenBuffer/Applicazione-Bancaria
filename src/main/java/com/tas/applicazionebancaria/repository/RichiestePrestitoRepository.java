package com.tas.applicazionebancaria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.RichiestePrestito;

@Repository("RichiestePrestitoRepository")
public interface RichiestePrestitoRepository extends JpaRepository<RichiestePrestito, Long>{

}
