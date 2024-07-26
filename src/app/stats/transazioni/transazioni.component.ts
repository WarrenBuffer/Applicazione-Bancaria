import { Component, Input } from '@angular/core';
import { ApiService } from '../../services/api.service';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-transazioni',
  templateUrl: './transazioni.component.html',
  styleUrl: './transazioni.component.css'
})
export class TransazioniComponent {
  @Input() stats: any;
  clienti : any;
  transazioni: any;
  data: any;
  options: any;

  constructor(private apiService: ApiService, private toastService: ToastService) {}

  ngOnInit() {
    this.clienti = this.stats.clienti;
    const documentStyle = getComputedStyle(document.documentElement);
    const textColor = documentStyle.getPropertyValue('--text-color');
    let accrediti = 0;
    let addebiti = 0;
    this.apiService.getTransazioni().subscribe({
      next: v => {
        if (v.code !== 0) this.toastService.showError(v.message);
        else {
          this.transazioni = v.message;
          this.transazioni.forEach((transazione: any) => {
            transazione.tipoTransazione === 'ADDEBITO' ? addebiti += 1 : accrediti += 1;
          });
          let percentuale;
          this.transazioni.length > 0 ? percentuale = accrediti / this.transazioni.length * 100 : percentuale = 0;
          this.data = {
            labels: [`Accredito ${parseFloat(percentuale + '').toFixed(2)}%`, `Addebito ${parseFloat(100 - percentuale + '').toFixed(2)}%`],
            datasets: [
              {
                data: [accrediti, addebiti],
                backgroundColor: [documentStyle.getPropertyValue('--primary-400'), documentStyle.getPropertyValue('--teal-200'), documentStyle.getPropertyValue('--green-500')],
                hoverBackgroundColor: [documentStyle.getPropertyValue('--primary-200'), documentStyle.getPropertyValue('--teal-100'), documentStyle.getPropertyValue('--green-400')]
              }
            ]
          };
      
          this.options = {
            plugins: {
              legend: {
                labels: {
                  usePointStyle: true,
                  color: textColor
                }
              },              
            }
          };
        }
      }
    })
  }

  calcAvgImporto(conti: any) {
    let tot = 0;
    let cum = 0;
    conti.forEach((conto: any) => {
      conto.transazioni?.forEach((transazione: any) => {
        tot += 1;
        cum += transazione.importo;
      })
    }) 
    return tot !== 0 ? cum / tot : 0;
  }

  calcTotTransazioni(conti: any) {
    let tot = 0;
    conti.forEach((conto: any) => {
      tot += conto.transazioni.length;
    }) 
    return tot;
  }

  calcTotAccrediti(conti: any) {
    let accrediti = 0;
    conti.forEach((conto: any) => {
      conto.transazioni?.forEach((transazione: any) => {
        if (transazione.tipoTransazione === 'ACCREDITO')
          accrediti += 1;
      })
    }) 

    return accrediti;
  }

  calcTotAddebiti(conti: any) {
    let addebiti = 0;
    conti.forEach((conto: any) => {
      conto.transazioni?.forEach((transazione: any) => {
        if (transazione.tipoTransazione === 'ADDEBITO')
          addebiti += 1;
      })
    }) 

    return addebiti;
  }
}
