package br.com.grupojcr.controller;

import java.util.Calendar;

import br.com.grupojcr.util.TreatDate;

public class Main {
	
	public static void main(String[] args) {
		Integer primeiroHorario =  507;
		Integer segundoHorario = 816;
		Integer terceiroHorario = 876;
		Integer ultimoHorario = 1125;
		
		Integer hora = primeiroHorario / 60;
		Integer minuto = primeiroHorario % 60;
		
		Calendar batida1 = Calendar.getInstance();
		batida1.set(Calendar.HOUR_OF_DAY, hora);
		batida1.set(Calendar.MINUTE, minuto);
		batida1.set(Calendar.SECOND, 0);
		batida1.set(Calendar.MILLISECOND, 0);
		
		System.out.println(TreatDate.format("HH:mm", batida1.getTime()));

		hora = segundoHorario / 60;
		minuto = segundoHorario % 60;
		
		Calendar batida2 = Calendar.getInstance();
		batida2.set(Calendar.HOUR_OF_DAY, hora);
		batida2.set(Calendar.MINUTE, minuto);
		batida2.set(Calendar.SECOND, 0);
		batida2.set(Calendar.MILLISECOND, 0);
		
		System.out.println(TreatDate.format("HH:mm", batida2.getTime()));

		hora = terceiroHorario / 60;
		minuto = terceiroHorario % 60;
		
		Calendar batida3 = Calendar.getInstance();
		batida3.set(Calendar.HOUR_OF_DAY, hora);
		batida3.set(Calendar.MINUTE, minuto);
		batida3.set(Calendar.SECOND, 0);
		batida3.set(Calendar.MILLISECOND, 0);
		
		System.out.println(TreatDate.format("HH:mm", batida3.getTime()));

		hora = ultimoHorario / 60;
		minuto = ultimoHorario % 60;
		
		Calendar batida4 = Calendar.getInstance();
		batida4.set(Calendar.HOUR_OF_DAY, hora);
		batida4.set(Calendar.MINUTE, minuto);
		batida4.set(Calendar.SECOND, 0);
		batida4.set(Calendar.MILLISECOND, 0);
		
		System.out.println(TreatDate.format("HH:mm", batida4.getTime()));
		
		Calendar calendarioAtual = Calendar.getInstance();
		Calendar periodoInicial = Calendar.getInstance();
		Calendar periodoFinal = Calendar.getInstance();
		Integer diaMes = calendarioAtual.get(Calendar.DAY_OF_MONTH);
		
		if(diaMes <= 14) {
			periodoInicial.set(Calendar.DAY_OF_MONTH, 15);
			periodoInicial.add(Calendar.MONTH, -1);
			System.out.println("Período Inicial: " + TreatDate.format("dd/MM/yyyy", periodoInicial.getTime()));
			
			periodoFinal.set(Calendar.DAY_OF_MONTH, 14);
			System.out.println("Período Final: " + TreatDate.format("dd/MM/yyyy", periodoFinal.getTime()));
		} else {
			periodoInicial.set(Calendar.DAY_OF_MONTH, 15);
			System.out.println("Período Inicial: " + TreatDate.format("dd/MM/yyyy", periodoInicial.getTime()));
			
			periodoFinal.set(Calendar.DAY_OF_MONTH, 14);
			periodoFinal.add(Calendar.MONTH, +1);
			System.out.println("Período Final: " + TreatDate.format("dd/MM/yyyy", periodoFinal.getTime()));
		}
		
		
	}

}
