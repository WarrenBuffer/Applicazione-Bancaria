package com.tas.applicazionebancaria.businesscomponent.model;

import org.junit.jupiter.api.Test;

class EnumTest {

	@Test
	void test() {
		Conto c=new Conto();
		c.setCodCliente(0);
		c.setCodConto(0);
		c.setEmailCliente("email");
		c.setSaldo(0);
		c.setTipoConto(Enum.CORRENTE);
		System.out.println(c);
	}

}
