package com.tas.applicazionebancaria.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tas.applicazionebancaria.businesscomponent.model.MovimentiConto;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@Repository("MovimentiContoRepository")
public interface MovimentiContoRepository extends JpaRepository<MovimentiConto, Long> {
	
	@Query(value="select * from movimenti_conto where cod_conto=?1 order by data_movimento desc limit 10", nativeQuery = true)
	List<MovimentiConto> findUltimi10(long codConto);
}
