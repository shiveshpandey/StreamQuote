package com.streamquote.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.streamquote.app.ZStreamingConfig;
import com.streamquote.model.OHLCquote;
import com.streamquote.model.StreamingQuote;
import com.streamquote.model.StreamingQuoteModeQuote;

public class StreamingQuoteDAOModeQuote implements IStreamingQuoteStorage {
	private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private static final String DB_URL = ZStreamingConfig
			.getStreamingQuoteDbUrl();
	private static final String USER = ZStreamingConfig
			.getStreamingQuoteDbUser();
	private static final String PASS = ZStreamingConfig
			.getStreamingQuoteDbPwd();
	private Connection conn = null;
	private static String quoteTable = null;

	/**
	 * constructor
	 */
	public StreamingQuoteDAOModeQuote() {
	}

	@Override
	public void initializeJDBCConn() {
		try {
			System.out
					.println("StreamingQuoteDAOModeQuote.initializeJDBCConn(): creating JDBC connection for Streaming Quote...");
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (ClassNotFoundException e) {
			System.out
					.println("StreamingQuoteDAOModeQuote.initializeJDBCConn(): ClassNotFoundException: "
							+ JDBC_DRIVER);
			e.printStackTrace();
		} catch (SQLException e) {
			System.out
					.println("StreamingQuoteDAOModeQuote.initializeJDBCConn(): SQLException on getConnection");
			e.printStackTrace();
		}
	}

	@Override
	public void closeJDBCConn() {
		if (conn != null) {
			try {
				System.out
						.println("StreamingQuoteDAOModeQuote.closeJDBCConn(): Closing JDBC connection for Streaming Quote...");
				conn.close();
			} catch (SQLException e) {
				System.out
						.println("StreamingQuoteDAOModeQuote.closeJDBCConn(): SQLException on conn close");
				e.printStackTrace();
			}
		} else {
			System.out
					.println("StreamingQuoteDAOModeQuote.closeJDBCConn(): WARNING: DB connection already null");
		}
	}

	@Override
	public void createDaysStreamingQuoteTable(String date) {
		if (conn != null) {
			Statement stmt;
			try {
				stmt = conn.createStatement();
				quoteTable = ZStreamingConfig
						.getStreamingQuoteTbNameAppendFormat(date);
				String sql = "CREATE TABLE "
						+ quoteTable
						+ " "
						+ "(Time time NOT NULL, "
						+ " InstrumentToken varchar(32) NOT NULL, "
						+ " LastTradedPrice DECIMAL(20,4) NOT NULL, "
						+ " LastTradedQty BIGINT NOT NULL, "
						+ " AvgTradedPrice DECIMAL(20,4) NOT NULL, "
						+ " Volume BIGINT NOT NULL, "
						+ " BuyQty BIGINT NOT NULL, "
						+ " SellQty BIGINT NOT NULL, "
						+ " OpenPrice DECIMAL(20,4) NOT NULL, "
						+ " HighPrice DECIMAL(20,4) NOT NULL, "
						+ " LowPrice DECIMAL(20,4) NOT NULL, "
						+ " ClosePrice DECIMAL(20,4) NOT NULL, "
						+ " PRIMARY KEY (InstrumentToken, Time)) "
						+ " ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
				stmt.executeUpdate(sql);
				System.out
						.println("StreamingQuoteDAOModeQuote.createDaysStreamingQuoteTable(): SQL table for Streaming quote created, table name: ["
								+ quoteTable + "]");
			} catch (SQLException e) {
				System.out
						.println("StreamingQuoteDAOModeQuote.createDaysStreamingQuoteTable(): ERROR: SQLException on creating Table, cause: "
								+ e.getMessage());
			}
		} else {
			System.out
					.println("StreamingQuoteDAOModeQuote.createDaysStreamingQuoteTable(): ERROR: DB conn is null !!!");
		}
	}

	@Override
	public void storeData(StreamingQuote quote) {
		if (conn != null && quote instanceof StreamingQuoteModeQuote) {
			StreamingQuoteModeQuote quoteModeQuote = (StreamingQuoteModeQuote) quote;

			try {
				String sql = "INSERT INTO "
						+ quoteTable
						+ " "
						+ "(Time, InstrumentToken, LastTradedPrice, LastTradedQty, AvgTradedPrice, "
						+ "Volume, BuyQty, SellQty, OpenPrice, HighPrice, LowPrice, ClosePrice) "
						+ "values(?,?,?,?,?,?,?,?,?,?,?,?)";
				PreparedStatement prepStmt = conn.prepareStatement(sql);

				prepStmt.setString(1, quoteModeQuote.getTime());
				prepStmt.setString(2, quoteModeQuote.getInstrumentToken());
				prepStmt.setBigDecimal(3, quoteModeQuote.getLtp());
				prepStmt.setLong(4, quoteModeQuote.getLastTradedQty());
				prepStmt.setBigDecimal(5, quoteModeQuote.getAvgTradedPrice());
				prepStmt.setLong(6, quoteModeQuote.getVol());
				prepStmt.setLong(7, quoteModeQuote.getBuyQty());
				prepStmt.setLong(8, quoteModeQuote.getSellQty());
				prepStmt.setBigDecimal(9, quoteModeQuote.getOpenPrice());
				prepStmt.setBigDecimal(10, quoteModeQuote.getHighPrice());
				prepStmt.setBigDecimal(11, quoteModeQuote.getLowPrice());
				prepStmt.setBigDecimal(12, quoteModeQuote.getClosePrice());

				prepStmt.executeUpdate();
				prepStmt.close();
			} catch (SQLException e) {
				System.out
						.println("StreamingQuoteDAOModeQuote.storeData(): ERROR: SQLException on Storing data to Table: "
								+ quote);
				System.out
						.println("StreamingQuoteDAOModeQuote.storeData(): [SQLException Cause]: "
								+ e.getMessage());
			}
		} else {
			if (conn != null) {
				System.out
						.println("StreamingQuoteDAOModeQuote.storeData(): ERROR: DB conn is null !!!");
			} else {
				System.out
						.println("StreamingQuoteDAOModeQuote.storeData(): ERROR: quote is not of type StreamingQuoteModeQuote !!!");
			}
		}
	}

	@Override
	public OHLCquote getOHLCDataByTimeRange(String instrumentToken,
			String prevTime, String currTime) {
		OHLCquote ohlcMap = null;

		if (conn != null) {
			try {
				Statement stmt = conn.createStatement();

				String openSql = "SELECT LastTradedPrice FROM " + quoteTable
						+ " WHERE Time >= '" + prevTime + "' AND Time <= '"
						+ currTime + "' AND InstrumentToken = '"
						+ instrumentToken + "' ORDER BY Time ASC LIMIT 1";
				ResultSet openRs = stmt.executeQuery(openSql);
				openRs.next();
				BigDecimal openQuote = openRs.getBigDecimal("LastTradedPrice");

				String highSql = "SELECT MAX(LastTradedPrice) FROM "
						+ quoteTable + " WHERE Time >= '" + prevTime
						+ "' AND Time <= '" + currTime
						+ "' AND InstrumentToken = '" + instrumentToken + "'";
				ResultSet highRs = stmt.executeQuery(highSql);
				highRs.next();
				BigDecimal highQuote = highRs.getBigDecimal(1);

				String lowSql = "SELECT MIN(LastTradedPrice) FROM "
						+ quoteTable + " WHERE Time >= '" + prevTime
						+ "' AND Time <= '" + currTime
						+ "' AND InstrumentToken = '" + instrumentToken + "'";
				ResultSet lowRs = stmt.executeQuery(lowSql);
				lowRs.next();
				BigDecimal lowQuote = lowRs.getBigDecimal(1);

				String closeSql = "SELECT LastTradedPrice FROM " + quoteTable
						+ " WHERE Time >= '" + prevTime + "' AND Time <= '"
						+ currTime + "' AND InstrumentToken = '"
						+ instrumentToken + "' ORDER BY Time DESC LIMIT 1";
				ResultSet closeRs = stmt.executeQuery(closeSql);
				closeRs.next();
				BigDecimal closeQuote = closeRs
						.getBigDecimal("LastTradedPrice");

				String volSql = "SELECT Volume FROM " + quoteTable
						+ " WHERE Time >= '" + prevTime + "' AND Time <= '"
						+ currTime + "' AND InstrumentToken = '"
						+ instrumentToken + "' ORDER BY Time DESC LIMIT 1";
				ResultSet volRs = stmt.executeQuery(volSql);
				volRs.next();
				Long volQuote = volRs.getLong(1);

				ohlcMap = new OHLCquote(openQuote, highQuote, lowQuote,
						closeQuote, volQuote);

				stmt.close();
			} catch (SQLException e) {
				ohlcMap = null;
				System.out
						.println("StreamingQuoteDAOModeQuote.getOHLCDataByTimeRange(): ERROR: SQLException on fetching data from Table, cause: "
								+ e.getMessage());
			}
		} else {
			ohlcMap = null;
			System.out
					.println("StreamingQuoteDAOModeQuote.getOHLCDataByTimeRange(): ERROR: DB conn is null !!!");
		}

		return ohlcMap;
	}

	@Override
	public List<StreamingQuote> getQuoteListByTimeRange(String instrumentToken,
			String prevTime, String currTime) {
		List<StreamingQuote> streamingQuoteList = new ArrayList<StreamingQuote>();

		if (conn != null) {
			try {
				Statement stmt = conn.createStatement();

				String openSql = "SELECT * FROM " + quoteTable
						+ " WHERE Time >= '" + prevTime + "' AND Time <= '"
						+ currTime + "' AND InstrumentToken = '"
						+ instrumentToken + "'";
				ResultSet openRs = stmt.executeQuery(openSql);
				while (openRs.next()) {
					String time = openRs.getString("Time");
					String instrument_Token = openRs
							.getString("InstrumentToken");
					BigDecimal lastTradedPrice = openRs
							.getBigDecimal("LastTradedPrice");
					Long lastTradedQty = openRs.getLong("LastTradedQty");
					BigDecimal avgTradedPrice = openRs
							.getBigDecimal("AvgTradedPrice");
					Long volume = openRs.getLong("Volume");
					Long buyQty = openRs.getLong("BuyQty");
					Long sellQty = openRs.getLong("SellQty");
					BigDecimal openPrice = openRs.getBigDecimal("OpenPrice");
					BigDecimal highPrice = openRs.getBigDecimal("HighPrice");
					BigDecimal lowPrice = openRs.getBigDecimal("LowPrice");
					BigDecimal closePrice = openRs.getBigDecimal("ClosePrice");

					StreamingQuote streamingQuote = new StreamingQuoteModeQuote(
							time, instrument_Token, lastTradedPrice,
							lastTradedQty, avgTradedPrice, volume, buyQty,
							sellQty, openPrice, highPrice, lowPrice, closePrice);
					streamingQuoteList.add(streamingQuote);
				}

				stmt.close();
			} catch (SQLException e) {
				streamingQuoteList = null;
				System.out
						.println("StreamingQuoteDAOModeQuote.getQuoteByTimeRange(): ERROR: SQLException on fetching data from Table, cause: "
								+ e.getMessage());
			}
		} else {
			streamingQuoteList = null;
			System.out
					.println("StreamingQuoteDAOModeQuote.getQuoteByTimeRange(): ERROR: DB conn is null !!!");
		}

		return streamingQuoteList;
	}

	@Override
	public void createDaysStreamingQuoteSignalTable(String date) {
		if (conn != null) {
			Statement stmt;
			try {
				stmt = conn.createStatement();
				quoteTable = ZStreamingConfig
						.getStreamingQuoteTbNameAppendFormat(date);
				String sql = "CREATE TABLE "
						+ quoteTable
						+ "TradingSignal"
						+ " "
						+ "(Time time NOT NULL, "
						+ " InstrumentToken varchar(32) NOT NULL, "
						+ " TradeSignal varchar(32) NOT NULL, "
						+ " ClosePrice varchar(32) NOT NULL, "
						+ " PRIMARY KEY (InstrumentToken, Time)) "
						+ " ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";
				stmt.executeUpdate(sql);
				System.out
						.println("StreamingQuoteDAOModeQuote.createDaysStreamingQuoteSignalTable(): SQL table for Streaming quote created, table name: ["
								+ quoteTable + "]");
			} catch (SQLException e) {
				System.out
						.println("StreamingQuoteDAOModeQuote.createDaysStreamingQuoteSignalTable(): ERROR: SQLException on creating Table, cause: "
								+ e.getMessage());
			}
		} else {
			System.out
					.println("StreamingQuoteDAOModeQuote.createDaysStreamingQuoteSignalTable(): ERROR: DB conn is null !!!");
		}
	}

	@Override
	public void storeSignalData(Date lastTickTime, String stockName,
			String tradeBuy, String closePrice) {
		// DateFormat dtFmt2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// dtFmt2.setTimeZone(TimeZone.getTimeZone("IST"));
		DateFormat dtFmt3 = new SimpleDateFormat("HH:mm:ss");
		dtFmt3.setTimeZone(TimeZone.getTimeZone("IST"));
		if (conn != null) {
			try {
				String sql = "INSERT INTO " + quoteTable + "TradingSignal"
						+ " "
						+ "(Time, InstrumentToken, TradeSignal,ClosePrice) "
						+ "values(?,?,?,?)";
				PreparedStatement prepStmt = conn.prepareStatement(sql);

				prepStmt.setString(1, dtFmt3.format(lastTickTime));
				prepStmt.setString(2, stockName);
				prepStmt.setString(3, tradeBuy);
				prepStmt.setString(4, closePrice);

				prepStmt.executeUpdate();
				prepStmt.close();
			} catch (SQLException e) {
				System.out
						.println("StreamingQuoteDAOModeQuote.storeSignalData(): [SQLException Cause]: "
								+ e.getMessage());
			}
		} else {
			if (conn != null) {
				System.out
						.println("StreamingQuoteDAOModeQuote.storeSignalData(): ERROR: DB conn is null !!!");
			} else {
				System.out
						.println("StreamingQuoteDAOModeQuote.storeSignalData(): ERROR: quote is not of type StreamingQuoteModeQuote !!!");
			}
		}
	}

	@Override
	public void setQuoteTableName(String date) {
		quoteTable = ZStreamingConfig.getStreamingQuoteTbNameAppendFormat(date);
	}

}
