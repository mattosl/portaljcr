package br.com.grupojcr.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import br.com.grupojcr.rm.BatidaRM;
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
		
//		input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dataInicio = periodoInicial.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate dataFinal = periodoFinal.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		long numOfDaysBetween = ChronoUnit.DAYS.between(dataInicio, dataFinal);
		System.out.println(numOfDaysBetween);
		
		List<LocalDate> datas = IntStream.iterate(0, i -> i + 1)
			      .limit(numOfDaysBetween + 1)
			      .mapToObj(i -> dataInicio.plusDays(i))
			      .collect(Collectors.toList()); 
		

		BatidaRM rm = new BatidaRM();
		rm.setData(TreatDate.montaData(15, 8, 2018));
		rm.setBatida(503);
		
		BatidaRM rm1 = new BatidaRM();
		rm1.setData(TreatDate.montaData(15, 8, 2018));
		rm1.setBatida(738);
		
		BatidaRM rm2 = new BatidaRM();
		rm2.setData(TreatDate.montaData(15, 8, 2018));
		rm2.setBatida(817);
		
		BatidaRM rm3 = new BatidaRM();
		rm3.setData(TreatDate.montaData(15, 8, 2018));
		rm3.setBatida(1112);

		BatidaRM rm4 = new BatidaRM();
		rm4.setData(TreatDate.montaData(16, 8, 2018));
		rm4.setBatida(499);
		
		BatidaRM rm5 = new BatidaRM();
		rm5.setData(TreatDate.montaData(16, 8, 2018));
		rm5.setBatida(753);
		
		BatidaRM rm6 = new BatidaRM();
		rm6.setData(TreatDate.montaData(16, 8, 2018));
		rm6.setBatida(863);
		
		BatidaRM rm7 = new BatidaRM();
		rm7.setData(TreatDate.montaData(17, 8, 2018));
		rm7.setBatida(1182);
		
		List<BatidaRM> batidas = new ArrayList<BatidaRM>();
		batidas.add(rm);
		batidas.add(rm1);
		batidas.add(rm2);
		batidas.add(rm3);
		batidas.add(rm4);
		batidas.add(rm5);
		batidas.add(rm6);
		batidas.add(rm7);
		DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/uuuu").withLocale(new Locale("pt", "BR"));
		
		System.out.println("DATA         DIA  ENT1  SAI1  ENT2  SAI2");
		for(LocalDate localDate : datas) {
			Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			List<Calendar> pontos = new ArrayList<Calendar>();
			for(BatidaRM batida : batidas) {
				if(TreatDate.isMesmaData(batida.getData(), date)) {
					hora = batida.getBatida() / 60;
					minuto = batida.getBatida() % 60;
					
					Calendar databatida = Calendar.getInstance();
					databatida.set(Calendar.HOUR_OF_DAY, hora);
					databatida.set(Calendar.MINUTE, minuto);
					databatida.set(Calendar.SECOND, 0);
					databatida.set(Calendar.MILLISECOND, 0);
					
					pontos.add(databatida);
				}
			}
			
			String nomeDia = localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault());
			StringBuilder sb = new StringBuilder();
			sb.append(localDate.format(formatters) + " - " + nomeDia.toUpperCase().substring(0, 3) + " ");
			for(Calendar ponto : pontos) {
				sb.append(TreatDate.format("HH:mm", ponto.getTime()) + " ");
			}
			System.out.println(sb.toString()); 
		}
		
		
	}

}
