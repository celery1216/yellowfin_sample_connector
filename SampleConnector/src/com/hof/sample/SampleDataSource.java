package com.hof.sample;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.hof.mi.thirdparty.interfaces.AbstractDataSet;
import com.hof.mi.thirdparty.interfaces.AbstractDataSource;
import com.hof.mi.thirdparty.interfaces.ColumnMetaData;
import com.hof.mi.thirdparty.interfaces.DataType;
import com.hof.mi.thirdparty.interfaces.FilterData;
import com.hof.mi.thirdparty.interfaces.FilterMetaData;
import com.hof.mi.thirdparty.interfaces.FilterOperator;
import com.hof.mi.thirdparty.interfaces.ScheduleDefinition;
import com.hof.mi.thirdparty.interfaces.ScheduleDefinition.FrequencyTypeCode;
import com.hof.pool.JDBCMetaData;
import com.hof.util.UtilString;
import com.ning.http.client.AsyncHttpClient;


public class SampleDataSource extends AbstractDataSource {

	private final static Logger log = Logger.getLogger(SampleDataSource.class);
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();
	private AsyncHttpClient httpClient = null;
	
	public String getDataSourceName() {
		
		return "Sample Saas";
		
	}
	
	
	public ScheduleDefinition getScheduleDefinition() { 
		return new ScheduleDefinition(FrequencyTypeCode.MINUTES, null, 5); 
	};
	
	
	public ArrayList<AbstractDataSet> getDataSets() {
		
		ArrayList<AbstractDataSet> p = new ArrayList<AbstractDataSet>();
		
		p.add(getAreaPopulation());
		
		return p;
		
	}


	private AbstractDataSet getAreaPopulation() {
		// TODO Auto-generated method stub
		
		AbstractDataSet dtSet = new AbstractDataSet() {
			
			public ArrayList<FilterMetaData> getFilters() {
				/*
				ArrayList<FilterMetaData> fm = new ArrayList<FilterMetaData>();
				fm.add(new FilterMetaData("STARTDATE", DataType.DATE, true, new FilterOperator[] {FilterOperator.EQUAL}));
				fm.add(new FilterMetaData("ENDDATE", DataType.DATE, true, new FilterOperator[] {FilterOperator.EQUAL}));
				
				return fm;
				*/
				return null;
			}
			
			
			public String getDataSetName() 
			{
				return "population_io";
			}
			
			public ArrayList<ColumnMetaData> getColumns() {
				
				ArrayList<ColumnMetaData> cm = new ArrayList<ColumnMetaData>();
				
				HashMap<String, String> mappingDimensions=getDimensions();
				HashMap<String, String> mappingMetrics=getMetrics();
				
				cm.add(new ColumnMetaData(mappingDimensions.get("females"), DataType.INTEGER));
				cm.add(new ColumnMetaData(mappingDimensions.get("age"), DataType.INTEGER));
				cm.add(new ColumnMetaData(mappingDimensions.get("males"), DataType.INTEGER));
				cm.add(new ColumnMetaData(mappingDimensions.get("year"), DataType.INTEGER));
				cm.add(new ColumnMetaData(mappingDimensions.get("total"), DataType.INTEGER));
				cm.add(new ColumnMetaData(mappingDimensions.get("country"), DataType.TEXT));
			
			return cm;
			
			}
			
			
			public Object[][] execute(List<ColumnMetaData> columns, List<FilterData> filters) 
			{

				Object data[][]=new Object[1][1];
				HashMap<String, String> dimensions=getDimensionsbyUIName();
				HashMap<String, String> metrics=getMetricsbyUIName();
				
				if (loadBlob("LASTRUN")==null){
					getData();
				}
				
				String byteb;
				if (loadBlob("JSON")!=null){
					byteb=new String(loadBlob("JSON"));
				}else {
					byteb=(String) getAttribute("JSON");
				}
				JSONArray jary = new JSONArray(new String(byteb));
				
				
				String metricsString="";
				String dimensionsString="";
				
				for (ColumnMetaData col:columns)
				{
					if (dimensions.containsKey(col.getColumnName()))
					{
						dimensionsString=dimensionsString+dimensions.get(col.getColumnName())+",";
					}
					
					if (metrics.containsKey(col.getColumnName()))
					{
						metricsString=metricsString+metrics.get(col.getColumnName())+",";
					}
				}
				/*
				if (metricsString.equals(""))
				{
					return null;
				}
				*/
				
				int num = jary.length();
				
				data = new Object[num][columns.size()];
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date dt = new Date();
				Calendar cal = Calendar.getInstance();
				cal.setTime(dt);
								
				for (int i=0; i<num; i++){
					for (int j=0; j<columns.size(); j++)
					{
						System.out.println("females:     "+columns.get(j).getColumnName());
						if (columns.get(j).getColumnName().equals("Females")){
							
							if(((JSONObject) jary.get(i)).has("females")==true){
								data[i][j]=((JSONObject) jary.get(i)).get("females");
							}
						}
						if (columns.get(j).getColumnName().equals("Age")){
							if(((JSONObject) jary.get(i)).has("age")==true){
								data[i][j]=((JSONObject) jary.get(i)).get("age");
							}
						}
						if (columns.get(j).getColumnName().equals("Males")){
							if(((JSONObject) jary.get(i)).has("males")==true){
								data[i][j]=((JSONObject) jary.get(i)).get("males");
							}
						}
						if (columns.get(j).getColumnName().equals("Year")){
							if(((JSONObject) jary.get(i)).has("year")==true){
								data[i][j]=((JSONObject) jary.get(i)).get("year");
							}
						}
						if (columns.get(j).getColumnName().equals("Total")){
							if(((JSONObject) jary.get(i)).has("total")==true){
								data[i][j]=((JSONObject) jary.get(i)).get("total");
							}
						}
						if (columns.get(j).getColumnName().equals("Country")){
							if(((JSONObject) jary.get(i)).has("country")==true){
								data[i][j]=((JSONObject) jary.get(i)).getString("country");
							}
						}
						/*
						if (columns.get(j).getColumnName().equals("country")){
							cal.add(Calendar.DATE, 0);
							java.sql.Date ts = java.sql.Date.valueOf(df.format(cal.getTime()));
							data[i][j]= ts;
						}
						
						if (columns.get(j).getColumnName().equals("GISpoint")){
							data[i][j]="point("+latitude+","+longitude+")";
						}
						*/
						
					}
				}
				
				return data;
				
				//} catch (IOException e) {
					// TODO Auto-generated catch block
					//log.error("Bad Query!");
				//}
				//return null;
			}
			
			@Override
			public boolean getAllowsDuplicateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean getAllowsAggregateColumns() {
				// TODO Auto-generated method stub
				return false;
			}
			
		};
		
		return dtSet;
	}
	
	public JDBCMetaData getDataSourceMetaData() {
		return new SampleMetaData();
	}


	public boolean authenticate() 
	{
		return true;
	}
	
	public void disconnect(){
		
	}

	public Map<String,Object> testConnection() throws IOException{
		
		Map<String,Object> p = new HashMap<String, Object>();
		
		String year = getYear();
		String country = getCountry();
		
		
		String apiurl = "http://api.population.io/1.0/population/";
		String requestUrl = apiurl + year + "/" + country + "/?format=json";
		
        HttpTransport transport = new NetHttpTransport();
        HttpRequestFactory factory = transport.createRequestFactory();
        GenericUrl url = new GenericUrl(requestUrl);

    	HttpRequest request;

		try {
			request = factory.buildGetRequest(url);
			request.getHeaders().setContentType("application/json");
			request.getHeaders().set("Accept-Language", "ja,en-US;q=0.8,en;q=0.6");

			System.out.println("request.getHeaders()     : "+request.getHeaders());
			
            HttpResponse response = request.execute();
            
            
            //System.out.println("Headers     : "+response.getHeaders());
            //System.out.println("Charset     : "+response.getContentCharset());
            //System.out.println("Encoding     : "+response.getContentEncoding());
            byte[] iso = response.parseAsString().getBytes("ISO-8859-1");
            String utf = new String(iso,"UTF-8");
           
            JSONArray obj = new JSONArray(utf);
            
            JSONObject jobj = (JSONObject) obj.get(0);
            
           
            saveBlob("JSON", utf.toString().getBytes());
            saveBlob("LASTRUN", new Date(System.currentTimeMillis()).toLocaleString().getBytes());
            p.put("取得状況", "取得できました" );
           
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			p.put("ERROR", "Can't retrieve the channels");
		}
		
		return p;
		
	}	
	
	public boolean autoRun(){
		
		if (loadBlob("LASTRUN")==null){
			getData();
		} else {
			
			Date curDt=new Date();
			Date lastrun=new Date(new String(loadBlob("LASTRUN")));
			long diff = curDt.getTime() - lastrun.getTime();
			//long hoursDiff=diff / (60 * 60 * 1000);
			long hoursDiff=diff / (5 * 60 * 1000);
			
			if (hoursDiff>1){
                getData();
			}
		}
		
		return true;
	}
	
	private void getData(){
			
			String year = getYear();
			String country = getCountry();
			
			
			String apiurl = "http://api.population.io/1.0/population/";
			String requestUrl = apiurl + year + "/" + country + "/?format=json";

            HttpTransport transport = new NetHttpTransport();
            HttpRequestFactory factory = transport.createRequestFactory();
            GenericUrl url = new GenericUrl(requestUrl);
            
            HttpRequest request;

            String utf = "";
            
            try {
    			request = factory.buildGetRequest(url);
    			request.getHeaders().setContentType("application/json");

                HttpResponse response = request.execute();
                
                byte[] iso = response.parseAsString().getBytes("ISO-8859-1");
                utf = new String(iso,"UTF-8");
               
                JSONArray obj = new JSONArray(utf);

            } catch (IOException e){
            	e.printStackTrace();
            } finally {
            	try {
            		transport.shutdown();
            	} catch(IOException e){
            		e.printStackTrace();
            	}
            }
            
            saveBlob("JSON", utf.toString().getBytes());
            saveBlob("LASTRUN", new Date(System.currentTimeMillis()).toLocaleString().getBytes());
	}
	
	private HashMap<String, String> getDimensions()
	{
		HashMap<String, String> dimensions=new HashMap<String, String>();

		dimensions.put("females", "Females");
		dimensions.put("country", "Country");
		dimensions.put("age", "Age");
		dimensions.put("males", "Males");
		dimensions.put("year", "Year");
		dimensions.put("total", "Total");

		return dimensions;
	}
	
	
	private HashMap<String, String> getDimensionsbyUIName()
	{
		HashMap<String, String> dimensions=new HashMap<String, String>();

		dimensions.put("Females", "females");
		dimensions.put("Country", "country");
		dimensions.put("Age", "age");
		dimensions.put("Males", "males");
		dimensions.put("Year", "year");
		dimensions.put("Total", "total");
		
		return dimensions;
	}

	
	
	private HashMap<String, String> getMetrics()
	{
		HashMap<String, String> metrics=new HashMap<String, String>();
		
		// all
		/*
		metrics.put("amount", "Amount");

		*/
		return metrics;
	}
	
	
	
	private HashMap<String, String> getMetricsbyUIName()
	{
		HashMap<String, String> metrics=new HashMap<String, String>();
		
		// all
		/*
		metrics.put("Amount", "amount");

		*/
		return metrics;
	}
	
/*
	private String getRefreshToken() {
		String refreshToken;
		if (loadBlob("REFRESH_TOKEN")!=null)
		{
			refreshToken=new String(loadBlob("REFRESH_TOKEN"));
		}
		
		else 
		{
			refreshToken=(String) getAttribute("REFRESH_TOKEN");
		}
		return refreshToken;
	}

	String getAccessToken() {
		String accessToken;
		if (loadBlob("ACCESS_TOKEN")!=null){
			accessToken=new String(loadBlob("ACCESS_TOKEN"));
		}else {
			accessToken=(String) getAttribute("ACCESS_TOKEN");
		}
		return accessToken;
	}
*/	
	String getYear() {
		String year;
		if (loadBlob("YEAR")!=null){
			year=new String(loadBlob("YEAR"));
		}else {
			year=(String) getAttribute("YEAR");
		}
		return year;
	}
	
	String getCountry() {
		String country;
		if (loadBlob("COUNTRY")!=null){
			country=new String(loadBlob("COUNTRY"));
		}else {
			country=(String) getAttribute("COUNTRY");
		}
		return country;
	}
	
	String getJson() {
		String json;
		if (loadBlob("JSON")!=null){
			json=new String(loadBlob("JSON"));
		}else {
			json=(String) getAttribute("JSON");
		}
		return json;
	}

}
