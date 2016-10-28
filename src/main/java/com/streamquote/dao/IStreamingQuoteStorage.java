package com.streamquote.dao;

import java.util.Date;
import java.util.List;

import com.streamquote.model.OHLCquote;
import com.streamquote.model.StreamingQuote;

public interface IStreamingQuoteStorage {

	public void initializeJDBCConn();

	public void closeJDBCConn();

	public void createDaysStreamingQuoteTable(String date);

	public void storeData(StreamingQuote quote);

	public OHLCquote getOHLCDataByTimeRange(String instrumentToken,
			String prevTime, String currTime);

	public List<StreamingQuote> getQuoteListByTimeRange(String instrumentToken,
			String prevTime, String currTime);

	void storeSignalData(Date lastTickTime, String stockName, String tradeBuy, String string);

	void createDaysStreamingQuoteSignalTable(String date);

	public void setQuoteTableName(String date);
}
