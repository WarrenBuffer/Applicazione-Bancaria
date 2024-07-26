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

      let saldo = 0;
      cliente.conti.forEach((conto: any) => {
        conti ++;

        saldo += conto.saldo;
        this.saldoTotale += conto.saldo;
        
        conto.transazioni.forEach((transazione: any) => {

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

      // saldo medio conti
      conti !== 0 ? this.saldoMedioConti = this.saldoTotale / conti : this.saldoMedioConti = 0;

    })
  }
}
