package com.hof.sample;

import java.io.IOException;

import org.json.JSONObject;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.hof.mi.interfaces.UserInputParameters.Parameter;
import com.hof.pool.DBType;
import com.hof.pool.JDBCMetaData;

public class SampleMetaData extends JDBCMetaData {

	
	private static String API_ENDPOINT = "http://api.population.io/1.0/population/";
	/*
	private static String AUTHORIZE_ENDPOINT = "oauth/authorize";
	private static String TOKEN_ENDPOINT = "oauth/token";
	private static String REFRESH = "oauth/token?grant_type=refresh_token";
	private static String REDIRECT_URI = "redirect_uri=http://localhost:8080/getToken.jsp";
	private static String SCOPE = "scope=read";
	private static String OAUTH_SUFFIX = "oauth/tokens";
	*/
	boolean initialised = false;
	public String code = "";
	
	
	
	public SampleMetaData() {
		
		super();
		
		sourceName = "Sample Saas";
		sourceCode = "Sample_Saas";
		driverName = "com.hof.sample.SampleDataSource";
		sourceType = DBType.THIRD_PARTY;
	}
	
	
	 public void initialiseParameters() 
	 {
		 
			
			//Token requestToken=new Token("", "");
			
		 	//String url= API_ENDPOINT+AUTHORIZE_ENDPOINT;	
				
					
			String inst="観測したい人口の年・国名を入力してください"; 
			
			addParameter(new Parameter("HELP", "Connection Instructions",  inst, TYPE_NUMERIC, DISPLAY_STATIC_TEXT, null, true));
			addParameter(new Parameter("YEAR", "1. 年",  "Enter the year of point you want to see", TYPE_TEXT, DISPLAY_TEXT_MED, null, true));
			addParameter(new Parameter("COUNTRY", "2. 国名",  "Enter the country of point you want to see", TYPE_TEXT, DISPLAY_TEXT_MED, null, true));
			
/*
			Parameter p = new Parameter("POSTPIN", "2. Generate authorize page", "Generate authorize page",TYPE_TEXT, DISPLAY_BUTTON,  null, true);
			p.addOption("BUTTONTEXT", "Generate URL");
	        addParameter(p);
			Parameter par = new Parameter("URL", "3. Go To Website", "Log in with your Account and create a Private Application",TYPE_UNKNOWN, DISPLAY_URLBUTTON,  null, true);
		    
		    par.addOption("BUTTONTEXT", "Go to ");
		    //par.addOption("BUTTONURL", url);
		    addParameter(par);

		    addParameter(new Parameter("AUTHORIZATION_CODE", "4. authorization code",  "Receive a authorization code", TYPE_TEXT, DISPLAY_TEXT_MED, null, true));
			addParameter(new Parameter("SECRETKEY", "5. Enter secret key",  "Enter the secret key you registered", TYPE_TEXT, DISPLAY_TEXT_MED, null, true));
	        p = new Parameter("VALIDPIN", "6. Validate Pin",  "Validate the PIN", TYPE_TEXT, DISPLAY_BUTTON, null, true);
	        p.addOption("BUTTONTEXT", "Verify PIN");
	        addParameter(p);
		    
	        addParameter(new Parameter("ACCESS_TOKEN", "7. access token",  "Receive a access token", TYPE_TEXT, DISPLAY_TEXT_LONG, null, true));
	        addParameter(new Parameter("REFRESH_TOKEN", "8. refresh token",  "Receive a refresh token", TYPE_TEXT, DISPLAY_TEXT_LONG, null, true));
*/			
	    }
	    
	    public String buttonPressed(String buttonName) throws Exception {
	    
	    	
	    	
    		
    		String year = (String)getParameterValue("YEAR");
    		String country = (String)getParameterValue("COUNTRY");
	    	
    		String accessurl = API_ENDPOINT + year + "/" + country;
    		
    		/*
	    	if (buttonName.equals("POSTPIN"))
	    	{
	    		String authurl= API_ENDPOINT+AUTHORIZE_ENDPOINT;
	    		authurl = authurl + "?client_id=" + clientId + "&redirect_uri=urn:ietf:wg:oauth:2.0:oob&response_type=code";
	    		System.out.println(authurl);
                getParameter("URL").addOption("BUTTONURL",authurl);

	    		return null;
	    	}
	    	
	    	
	    	
	    	if (buttonName.equals("VALIDPIN"))
	    	{
	    		String validKey = (String)getParameterValue("AUTHORIZATION_CODE");
	    		String secretKey = (String)getParameterValue("SECRETKEY");

	    		//String authUrl = API_ENDPOINT + TOKEN_ENDPOINT;
	    		String authUrl = "https://secure.freee.co.jp/oauth/token";

                HttpTransport transport = new NetHttpTransport();
                HttpRequestFactory factory = transport.createRequestFactory();
                GenericUrl url = new GenericUrl(authUrl);
                
                try {
                	String requestBody = "{\"grant_type\": \"" + GRANT_TYPE + "\", \"code\": \""+ validKey + 
                			"\",\"client_id\": \"" + clientId + "\", \"client_secret\": \"" + secretKey + "\", \"redirect_uri\":\"oauth:2.0:oob\"}";
                					
                	System.out.println(requestBody);
                	HttpRequest request = factory.buildPostRequest(url, ByteArrayContent.fromString(null, requestBody));
                	request.getHeaders().setContentType("application/json");
                    HttpResponse response = request.execute();
                    
                    JSONObject obj = new JSONObject(response.parseAsString());
                    System.out.println(obj);
        			if (obj.has("access_token"))
        			{
        				setParameterValue("ACCESS_TOKEN", obj.getString("access_token"));
        			}
        			if (obj.has("refresh_token"))
        			{
        				setParameterValue("REFRESH_TOKEN", obj.getString("refresh_token"));
        			}

                } catch (IOException e){
                	e.printStackTrace();
                } finally {
                	try {
                		transport.shutdown();
                	} catch(IOException e){
                		e.printStackTrace();
                	}
                }

	    	}
	    	*/
	    	
	        return null;
	    }
	    
	    @Override
		public byte[] getDatasourceIcon() {
			String str = "";
			//return str.getBytes();
			return null;
	    }

}

