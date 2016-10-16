package com.streamquote.jettyhandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.streamquote.app.ZStreamingConfig;
import com.streamquote.app.ZStreamingQuoteControl;
import com.streamquote.model.OHLCquote;

public class TimeRangeOHLCActionHandler extends ContextHandler {
	private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	private TimeZone timeZone = TimeZone.getTimeZone("IST");
	
	/**
	 * constructor
	 */
	public TimeRangeOHLCActionHandler(){
		setContextPath(ZStreamingConfig.getJettyServerTimeRangeOHLCURL());
		dateFormat.setTimeZone(timeZone);
	}
	
	@Override
	public void doHandle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String reqMethod = request.getMethod();
		
		//map request params to values
		Map<String, String> queryValMap = getQueryParameters(request);
		
		//Get the requested format for data
		String dataFormat = queryValMap.get(ZStreamingConfig.getJettyServerTimeRangeOHLCformatReqParam());
		
		//Get Time Range requested
		String fromTime = queryValMap.get(ZStreamingConfig.getJettyServerTimeRangeOHLCfromTimeReqParam());
		String toTime = queryValMap.get(ZStreamingConfig.getJettyServerTimeRangeOHLCtoTimeReqParam());
		String instrumentToken = queryValMap.get(ZStreamingConfig.getJettyServerTimeRangeOHLCinstrumentReqParam());

		if(reqMethod.equals("GET")){
			//GET method
			if(ZStreamingConfig.isWebServiceLogsPrintable()){
				System.out.println("TimeRangeOHLCActionHandler.doHandle(): ZStreamingQuote Time Range OHLC [GET]: Requested Format: " + 
					"[" + dataFormat + "] fromTime: [" + fromTime + "] toTime: [" + toTime + "] instrumentToken: [" + instrumentToken + 
					"] - [" + dateFormat.format(Calendar.getInstance(timeZone).getTime()) + "]");
			}
			
			OHLCquote quote = ZStreamingQuoteControl.getInstance().getOHLCDataByTimeRange(instrumentToken, fromTime, toTime);
			String outData = null;
			if(quote == null){
				outData = "<h1>Requested Data could NOT be fetched, may be DB problem</h1>";
			} else{
				if(dataFormat.equals("json")){
					//format JSON
					outData = formatQuoteToJSON(quote);
				} /*else if(dataFormat.equals("csv")){
					//format CSV
					//outData = formatQuoteToCSV(quote);
				}*/ else{
					outData = "<h1>Requested Format " + dataFormat + " NOT supported, only csv or json</h1>";
					System.out.println("TimeRangeOHLCActionHandler.doHandle(): ERROR: [" + dataFormat + "] NOT Supported");
				}
			}
			
			//write HTTP o/p back to client
			PrintWriter out = response.getWriter();
			out.println(outData);
		} else if(reqMethod.equals("POST")){
			//POST method not handled currently
		} else{
			//Default - other handlers not supported
			System.out.println("TimeRangeOHLCActionHandler.doHandle(): ERROR: Request method not proper: " + reqMethod);
		}

		response.setContentType("text/html; charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		
		baseRequest.setHandled(true);
	}
	
	/**
	 * formatQuoteToJSON - convert quote to JSON
	 * @param quote
	 * @return JSON formatted quote
	 */
	private String formatQuoteToJSON(OHLCquote quote){
		String jsonData = null;
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			jsonData = mapper.writeValueAsString(quote);
		} catch (JsonProcessingException e) {
			System.out.println("TimeRangeOHLCActionHandler.formatQuoteToJSON(): ERROR: JsonProcessingException on quote !!!");
			e.printStackTrace();
		}
		
		return jsonData;
	}
	
	/**
	 * formatQuoteToCSV - convert quote to CSV
	 * @param quote
	 * @return CSV formatted Quote
	 */
	private String formatQuoteToCSV(OHLCquote quote){
		String csvData = null;
		CsvMapper mapper = new CsvMapper();
		CsvSchema schema = mapper.schemaFor(OHLCquote.class).withHeader().withColumnSeparator(',');
		
		try {
			csvData = mapper.writer(schema).writeValueAsString(quote);
		} catch (JsonProcessingException e) {
			System.out.println("TimeRangeOHLCActionHandler.formatQuoteToCSV(): ERROR: JsonProcessingException on quote");
			e.printStackTrace();
		}
		
		return csvData;
	}
	
	/**
	 * getQueryParameters - map of query parameter to value
	 * @param request
	 * @return map of request parameters
	 */
	private Map<String, String> getQueryParameters(HttpServletRequest request){
		Map<String, String> queryParameters = new HashMap<>();
	    String queryString = request.getQueryString();

	    if (StringUtils.isEmpty(queryString)) {
	    	System.out.println("TimeRangeOHLCActionHandler.getQueryParameters(): ERROR: query string is empty !!!");
	        return null;
	    }

	    String[] parameters;
	    if(queryString.contains("&")){
	    	parameters = queryString.split("&");
	    } else{
	    	parameters = queryString.split("%26");
	    }
	    for (String parameter : parameters) {
	        String[] keyValuePair = parameter.split("=");
	        queryParameters.put(keyValuePair[0], keyValuePair[1]);
	    }
	    return queryParameters;
	}
}
