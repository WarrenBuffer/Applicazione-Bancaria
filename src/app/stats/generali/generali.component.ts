import { Component, Input, OnInit } from '@angular/core';
import { Cliente } from '../../model/cliente';

@Component({
  selector: 'app-generali',
  templateUrl: './generali.component.html',
  styleUrl: './generali.component.css'
})
export class GeneraliComponent implements OnInit {
  @Input() stats: any;

  transazioniPerMese = new Array<number>(12).fill(0);
  tableTransactions = [this.transazioniPerMese];
  intervalli = new Array<number>(5).fill(0);
  tableIntervalli = [this.intervalli];
  clienteSaldoPiuAlto: string = 'Nessun cliente presente';
  ultimaTransazione: Date = new Date('1800-01-01T00:00:00');
  totNTransazioni: number = 0;
  totImportoTransazioni: number = 0;
  saldoMedioConti: number = 0;
  contiPositivi: number = 0;
  contiNegativi: number = 0;
  saldoTotale: number = 0;

  ngOnInit(): void {
    this.stats.importoTransazioniPerMese.forEach((mese: any) => {
      this.transazioniPerMese[Number(mese.id) - 1] = mese.importo;
    });
    this.calcStats();
  }


  calcStats() {
    let conti = 0;
    this.stats.clienti.forEach((cliente: any) => {
      let tmpMaxSaldo = 0;

      cliente.conti.forEach((conto: any) => {
        let saldo = 0;
        conti ++;

        conto.transazioni.forEach((transazione: any) => {
          saldo += conto.saldo;
          this.saldoTotale += conto.saldo;

          this.totNTransazioni++;
          this.totImportoTransazioni += transazione.importo;

          const tmpDate = new Date(transazione.dataTransazione);
          if (tmpDate >= this.ultimaTransazione) this.ultimaTransazione = tmpDate;
        })

        conto.transazioniBancarie.forEach((transazione: any) => {
          this.totNTransazioni++;
          this.totImportoTransazioni += transazione.importo;

          const tmpDate = new Date(transazione.dataTransazione);
          if (tmpDate >= this.ultimaTransazione) this.ultimaTransazione = tmpDate;
        })

        // cliente con saldo più alto
        if (saldo >= tmpMaxSaldo) {
          this.clienteSaldoPiuAlto = cliente.emailCliente;
          tmpMaxSaldo = saldo;
        }

        // clienti per intervallo di saldo
        if (saldo <= 1000) this.intervalli[0]++;
        else if (saldo <= 5000) this.intervalli[1]++;
        else if (saldo <= 25000) this.intervalli[2]++;
        else if (saldo <= 100000) this.intervalli[3]++;
        else this.intervalli[4]++;

        // clienti saldo positivo/negativo
        saldo >= 0 ? this.contiPositivi ++ : this.contiNegativi ++;

      })

      // saldo medio conti
      conti !== 0 ? this.saldoMedioConti = this.saldoTotale / conti : this.saldoMedioConti = 0;

    })
  }

/*   calcClientPerIntervallo() {
    this.stats.clienti.forEach((cliente: any) => {
      let saldo = 0;
      cliente.conti.forEach((conto: any) => {
        saldo += conto.saldo;
      })
      if (saldo <= 1000) this.intervalli[0]++;
      else if (saldo <= 5000) this.intervalli[1]++;
      else if (saldo <= 25000) this.intervalli[2]++;
      else if (saldo <= 100000) this.intervalli[3]++;
      else this.intervalli[4]++;
    });
  }

  calcSaldoPiuAlto(clienti: any): Cliente {
    let max = 0;
    let maxCliente!: Cliente;

    clienti.forEach((cliente: any) => {
      let saldo = 0;
      cliente.conti.forEach((conto: any) => {
        saldo += conto.saldo;
      })
      if (saldo >= max) {
        maxCliente = cliente;
        max = saldo;
      }
    })
    return maxCliente;
  }

  calcUltimaTransazione(clienti: any): Date {
    let ultimaTransazione = new Date('1800-01-01T00:00:00');;

    clienti.forEach((cliente: any) => {
      cliente.conti.forEach((conto: any) => {
        conto.transazioni.forEach((transazione: any) => {
          const tmpDate = new Date(transazione.dataTransazione);
          if (tmpDate >= ultimaTransazione) ultimaTransazione = tmpDate;
        })
        conto.transazioniBancarie.forEach((transazione: any) => {
          const tmpDate = new Date(transazione.dataTransazione);
          if (tmpDate >= ultimaTransazione) ultimaTransazione = tmpDate;
        })
      })
    })
    return ultimaTransazione;
  }

  calcTotTransazioni(clienti: any): string {
    let totTransazioni = 0;
    let totImporto = 0;
    clienti.forEach((cliente: any) => {
      cliente.conti.forEach((conto: any) => {
        conto.transazioni.forEach((transazione: any) => {
          totTransazioni += 1;
          totImporto += transazione.importo;
        })
        conto.transazioniBancarie.forEach((transazione: any) => {
          totTransazioni += 1;
          totImporto += transazione.importo;
        })
      })
    })

    return `${totTransazioni} - $${totImporto}`;
  }

  calcSaldoMedioConti(clienti: any) {
    let totSaldo = 0;
    let totConti = 0;

    clienti.forEach((cliente: any) => {
      cliente.conti.forEach((conto: any) => {
        totSaldo += conto.saldo;
        totConti += 1;
      })
    })

    return totConti !== 0 ? totSaldo / totConti : 0;
  }

  calcPositivoNegativo(clienti: any) {
    let positivi = 0;
    let negativi = 0;

    clienti.forEach((cliente: any) => {
      let saldo = 0;
      cliente.conti.forEach((conto: any) => {
        saldo += conto.saldo;
      })
      if (saldo >= 0) positivi++;
      else negativi++;
    })

    return `${positivi} - ${negativi}`;
  }

  calcSaldoTotale(clienti: any) {
    let saldo = 0;

    clienti.forEach((cliente: any) => {
      cliente.conti.forEach((conto: any) => {
        saldo += conto.saldo;
      })
    })

    return saldo;
  } */
}
