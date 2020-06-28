package net.m21xx.finance.stocks.report.controllers;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.m21xx.finance.stocks.report.enums.OrderType;
import net.m21xx.finance.stocks.report.model.Order;
import net.m21xx.finance.stocks.report.repository.OrdersRepository;

@RestController
@RequestMapping(path = "/orders", produces = "application/json")
public class OrdersController {

	@Autowired
	private OrdersRepository ordersRepo;
	
	@GetMapping("/")
	public List<Order> getAll() {
		return ordersRepo.findAll();
	}
	
	@GetMapping("/export")
	public String exportAll() {
		StringBuilder sb = new StringBuilder();
		
		SimpleDateFormat fmtDate = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat fmtPrice = new DecimalFormat("#,##0.00",
				new DecimalFormatSymbols(new Locale("pt", "BR")));
		
		ordersRepo.setOrderBy(" date ASC, ordertype ASC ");
		List<Order> orders = ordersRepo.findAll();
		
		for (Order order : orders) {
			sb.append(String.format("%s,%s,\"%s\",%d,%s,0\n", order.getStock().toUpperCase(),
					fmtDate.format(order.getDate()), fmtPrice.format(order.getPrice().doubleValue()),
					order.getCount(), OrderType.COMPRA.equals(order.getOrderType()) ? 
							"Compra" : "Venda"));
		}
		
		return sb.toString();
	}
	
}
