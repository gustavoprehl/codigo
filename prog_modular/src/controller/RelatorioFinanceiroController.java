package controller;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import Dao.Reservas;
import model.Reserva;
import views.NovaReserva;
import views.RelatorioFinanceiro;
import Dao.ReservasFinalizadas;

public class RelatorioFinanceiroController {
	
	
	private RelatorioFinanceiro view;
	private ReservasFinalizadas reservasfinalizadas;
	
	public RelatorioFinanceiroController() {
		this.reservasfinalizadas = ReservasFinalizadas.getInstance();
		this.view = new RelatorioFinanceiro();
		this.view.setVisible(true);
		preencherDados();
	}
	
	private void preencherDados() {
		imprimirDados(this.reservasfinalizadas.getReservasFinalizadas());
	}
	
	public void imprimirDados(List<Reserva> reservas) {
        String[] columnNames = {"Id", "Data Inicio", "Data Fim", "Valor"};

        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableModel.setColumnIdentifiers(columnNames);
        reservas.forEach(reserva -> {
            tableModel.addRow(new String[]{String.valueOf(reserva.getProprietario().getNome()), String.valueOf(reserva.getProprietario().getNome()), reserva.getProprietario().getNome(), 
            		reserva.getProprietario().getNome()});
        });

        this.view.getTableRel().setModel(tableModel);
    }
	
	public static void calcularRecebimentos(List<Venda> vendas) {
        double totalVendas = 0;
        double totalReceberImediato = 0;
        double totalReceberFuturo = 0;

        for (Venda venda : vendas) {
            totalVendas += venda.valor;
            double valorRecebido = 0;
            int prazo = 0;

            switch (venda.metodoPagamento.toLowerCase()) {
                case "dinheiro":
                    valorRecebido = venda.valor;
                    prazo = 0;
                    break;
                case "pix":
                    valorRecebido = venda.valor * 0.9855;
                    if (valorRecebido > venda.valor - 10) {
                        valorRecebido = venda.valor - 10;
                    }
                    prazo = 0;
                    break;
                case "débito":
                    valorRecebido = venda.valor * 0.986;
                    prazo = 14;
                    break;
                case "crédito":
                    valorRecebido = venda.valor * 0.969;
                    prazo = 30;
                    break;
                default:
                    System.out.println("Método de pagamento desconhecido: " + venda.metodoPagamento);
                    continue;
            }

            if (prazo == 0) {
                totalReceberImediato += valorRecebido;
            } else {
                totalReceberFuturo += valorRecebido;
            }

            System.out.printf("Venda de R$ %.2f via %s: Recebimento de R$ %.2f em %d dias\n", 
                venda.valor, venda.metodoPagamento, valorRecebido, prazo);
        }

        System.out.printf("Total de vendas do dia: R$ %.2f\n", totalVendas);
        System.out.printf("Total a receber imediatamente: R$ %.2f\n", totalReceberImediato);
        System.out.printf("Total a receber em datas futuras: R$ %.2f\n", totalReceberFuturo);
    }
}
