package com.streamquote.jetty;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;

import com.streamquote.app.ZStreamingConfig;
import com.streamquote.jettyhandler.ProcessStartActionHandler;
import com.streamquote.jettyhandler.ProcessStopActionHandler;
import com.streamquote.jettyhandler.TimeRangeOHLCActionHandler;
import com.streamquote.jettyhandler.TimeRangeStreamingQuoteActionHandler;

public class JettyMain {

	/**
	 * Main Jetty Server method
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Server server = new Server(ZStreamingConfig.getJettyServerPortNum());

		HandlerCollection handlers = new HandlerCollection();

		// Add a handler for context "/zstreamingquote/start/"
		// Can be accessed using start(win) or wget(unix)
		// "http://localhost:8080/zstreamingquote/start/?apikey=1234dgfh&userid=DR1234&publictoken=gdhjkckwkljbdew32988yajj"
		handlers.addHandler(new ProcessStartActionHandler());

		// Add a handler for context "/zstreamingquote/stop/"
		// Can be accessed using start(win) or wget(unix)
		// "http://localhost:8080/zstreamingquote/stop/"
		handlers.addHandler(new ProcessStopActionHandler());

		// Add a handler for context "/zstreamingquote/timerangeohlc/"
		// Can be accessed using start(win) or wget(unix)
		// "http://localhost:8080/zstreamingquote/timerangeohlc/?format=json&instrument=2342&from=10:00:00&to=14:00:00"
		handlers.addHandler(new TimeRangeOHLCActionHandler());

		// Add a handler for context "/zstreamingquote/timerangestreamingquote/"
		// Can be accessed using start(win) or wget(unix)
		// "http://localhost:8080/zstreamingquote/timerangestreamingquote/?format=json&instrument=2342&from=10:00:00&to=14:00:00"
		handlers.addHandler(new TimeRangeStreamingQuoteActionHandler());

		server.setHandler(handlers);

		// Start the server
		server.start();
		server.join();
	}
}
