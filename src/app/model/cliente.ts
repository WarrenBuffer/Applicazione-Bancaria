export class Cliente {
    codCliente!: number;
    nomeCliente!: string;
    cognomeCliente!: string;
    emailCliente!: string;
    passwordCliente!: string;
    tentativiErrati!: number;
    accountBloccato!: boolean;

    constructor(emailCliente: string = '', passwordCliente: string = '') {
        this.emailCliente = emailCliente;
        this.passwordCliente = passwordCliente;
      }
}
