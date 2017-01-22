package com.tickets.controller;

import com.tickets.dao.ChartsDAO;
import com.tickets.dao.TicketDAOHibernate;
import com.tickets.model.ChartKeyValue;
import com.tickets.model.Filter;
import com.tickets.model.Ticket;
import com.tickets.utils.SaveToFile;
import com.tickets.validator.NewTicketValidator;
import com.tickets.validator.TicketFilterValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@Import(com.tickets.validator.TicketFilterValidator.class)
public class TicketController {
	@Autowired
	private TicketDAOHibernate ticketDAOHibernate;
	@Autowired
	private ChartsDAO chartsDAO;
	@Autowired
	private NewTicketValidator ticketFormValidator;
	@Autowired
	private TicketFilterValidator ticketFilterValidator;
	@Autowired
	private Filter ticketFilter;

	private Calendar calendar = Calendar.getInstance();
	private List<Ticket> listTicket;

	@InitBinder("TicketForm")
	public void initBinder(WebDataBinder binder){
		binder.setValidator(ticketFormValidator);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
	@InitBinder
		public void homeBinder(){
		listTicket = ticketDAOHibernate.list(ticketFilter.getCondition());
//		listTicket = ticketDAO.list(ticketFilter.getCondition());
	}

/**
 * Home mapping
 */
	@RequestMapping(value="/")
	public String redirect(){
		return "redirect:/home";
	}

	@RequestMapping(value="/home", method = RequestMethod.GET)
	public ModelAndView listTicket(ModelAndView model) throws IOException{

		model.addObject("currentDate",calendar.getTime());
		model.addObject("filter",ticketFilter);
		model.addObject("listTicket", listTicket);
		model.addObject("title", "Tickets list");
		model.setViewName("home");

		return model;
	}

/**
 * Ticket actions New,Edit,Delete,Save
 */
	@RequestMapping(value = "**/newTicket", method = RequestMethod.GET)
	public ModelAndView newTicket(ModelAndView model) {
		Ticket newTicket = new Ticket();
		SimpleDateFormat printFormat = new SimpleDateFormat("yyyyMMdd_kkmmss");
		Date date = new Date();
		newTicket.setNumber(System.getProperty("user.name") + "_" + printFormat.format(date));
		newTicket.setReportedBy(System.getProperty("user.name"));
		newTicket.setTstatus("In queue");
		model.addObject("TicketForm", newTicket);
		model.setViewName("TicketForm");
		model.addObject("clusters", Ticket.getClustersList());
		model.addObject("statuses", Ticket.getStatusesList());
		model.addObject("priorities", Ticket.getPrioritiesList());
		return model;
	}

    @RequestMapping(value = "**/saveTicket", method = RequestMethod.POST)
    public ModelAndView CheckForm(@ModelAttribute ("TicketForm") @Validated Ticket ticket, BindingResult result
			, ModelAndView model) {
        if (result.hasErrors()) {
        	ticket.initModelList();
			model.setViewName("TicketForm");
			model.addObject("clusters", Ticket.getClustersList());
			model.addObject("statuses", Ticket.getStatusesList());
			model.addObject("priorities", Ticket.getPrioritiesList());
			return model;
        }
        else {
//				ticketDAO.saveOrUpdate(ticket);
			ticketDAOHibernate.saveOrUpdate(ticket);
			return new ModelAndView("redirect:/");
        }

    }
	
	@RequestMapping(value = "**/deleteTicket", method = RequestMethod.GET)
	public ModelAndView deleteTicket(HttpServletRequest request) {
		int ticketId = Integer.parseInt(request.getParameter("id"));
		ticketDAOHibernate.delete(ticketId);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping(value = "**/editTicket", method = RequestMethod.GET)
	public ModelAndView editTicket(HttpServletRequest request) {
		int ticketId = Integer.parseInt(request.getParameter("id"));
//		Ticket ticket = ticketDAO.get(ticketId);
		Ticket ticket = ticketDAOHibernate.get(ticketId);
		ModelAndView model = new ModelAndView("TicketForm");
		model.addObject("clusters", Ticket.getClustersList());
		model.addObject("statuses", Ticket.getStatusesList());
		model.addObject("priorities", Ticket.getPrioritiesList());
		model.addObject("TicketForm", ticket);
		
		return model;
	}

/**
 * Charts Data mappring
 */
	@RequestMapping(value = "/charts**", method = RequestMethod.GET)
	public ModelAndView chartsPage() {
		List<ChartKeyValue> progressData = chartsDAO.getProgressData();
		List<ChartKeyValue> priorityData = chartsDAO.getPriorityData();
		List<ChartKeyValue> prioritySolveData = chartsDAO.getPrioritySolveData();

		ModelAndView model = new ModelAndView();
		model.addObject("progressDataList", progressData);
		model.addObject("priorityDataList", priorityData);
		model.addObject("prioritySolveDataList", prioritySolveData);
		model.addObject("title", "Basic KPI");
		model.setViewName("charts/BasicKpi");

		return model;
	}

/**
 * Ticket main table filter mapping and apply
 */
	@RequestMapping(value = "/filter**", method = RequestMethod.GET)
	public ModelAndView newFilter(ModelAndView model) {
		List<String> clusters = new ArrayList<String>();
		List<String> priorities = new ArrayList<String>();
		List<String> statuses = new ArrayList<String>();
		clusters.addAll(Ticket.getClustersList());
		priorities.addAll(Ticket.getPrioritiesList());
		statuses.addAll(Ticket.getStatusesList());
		model.addObject("filter", ticketFilter);
		model.setViewName("filter/TicketFilter");
		model.addObject("clusters", clusters);
		model.addObject("priorities", priorities);
		model.addObject("statuses", statuses);
		return model;
	}

	@RequestMapping(value = "/ApplyFilter", method = RequestMethod.POST)
	public ModelAndView CheckForm(@ModelAttribute("filter") Filter filter, BindingResult result
			, ModelAndView model) {
		TicketFilterValidator filterValidator = new TicketFilterValidator();
		filterValidator.validate(filter, result);

		if (result.hasErrors()){
			List<String> clusters = new ArrayList<String>();
			List<String> priorities = new ArrayList<String>();
			List<String> statuses = new ArrayList<String>();
			clusters.addAll(Ticket.getClustersList());
			priorities.addAll(Ticket.getPrioritiesList());
			statuses.addAll(Ticket.getStatusesList());

			model.setViewName("filter/TicketFilter");
			model.addObject("filter", filter);
			model.addObject("clusters", clusters);
			model.addObject("priorities", priorities);
			model.addObject("statuses", statuses);
			filter.setCondition();
			return model;
		}
		else {
			filter.setCondition();
			model.addObject("filter", filter);
			return new ModelAndView("redirect:/");
		}
	}
/**
	Export to excel file
 */
	@RequestMapping(value = "**/export", method = RequestMethod.GET)
	public String getFile(HttpServletRequest request,
						  HttpServletResponse response) throws IOException{
		int BUFFER_SIZE = 4096;
//		ServletContext context = request.getServletContext();
		ServletContext context = request.getSession().getServletContext();
		String appPath = context.getRealPath("");
		String filePath = "/Export.csv";
		String fullPath = appPath + filePath;


		SaveToFile sv = new SaveToFile(listTicket,fullPath);
		sv.saveFile();

		File downloadFile = new File(fullPath);
		FileInputStream inputStream = new FileInputStream(downloadFile);

		// get MIME type of the file
		String mimeType = context.getMimeType(fullPath);
		if (mimeType == null) {
			// set to binary type if MIME mapping not found
			mimeType = "application/octet-stream";
		}

		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());

		// set headers for the response
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"",
				downloadFile.getName());
		response.setHeader(headerKey, headerValue);

		// get output stream of the response
		OutputStream outStream = response.getOutputStream();

		byte[] buffer = new byte[BUFFER_SIZE];
		int bytesRead = -1;

		// write bytes read from the input stream into the output stream
		while ((bytesRead = inputStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, bytesRead);
		}

		inputStream.close();
		outStream.close();

		return "redirect:/home";
	}
}
