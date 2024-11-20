package io.paletaweb.client;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Multimap;

import io.paleta.logging.Logger;
import io.paleta.model.SharedConstant;
import io.paleta.util.Check;
import io.paletaweb.PaletaWebVersion;
import io.paletaweb.SystemService;
import io.paletaweb.service.SettingsService;
import jakarta.annotation.PostConstruct;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@Service
public class PaletaClientService implements SystemService {
			
	private static final int BUFFER_SIZE = 8192;
	
	private static final Logger logger = Logger.getLogger(PaletaClientService.class.getName());

	/** 
	 * 	CLUB
	 */

	private static final String API_CLUB_GETBYNAME							[] = {"club", "getbyname"};
	private static final String API_CLUB_LIST								[] = {"club", "list"};
	private static final String API_CLUB_GETBYID							[] = {"club", "get"};
	private static final String API_CLUB_SAVE								[] = {"club", "save"};
	private static final String API_CLUB_CREATE								[] = {"club", "create"};
	private static final String API_CLUB_EXISTS								[] = {"exists", "exists"};
	
	/** 
	 * 	TABLA
	 */
	 	
		
	
	/** ---------------------------------------------------- */
	
	private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
	private static final int HTTP_CACHE_SIZE = 200 * 1024 * 1024; // 200 mb
	
    /** default network I/O timeout is 15 minutes */
	public static final int DEFAULT_CONNECTION_TIMEOUT = 15 * 60;
	public static final String DEFAULT_USER_AGENT = "PaletaWeb (" + System.getProperty("os.arch") + "; " + System.getProperty("os.arch") + ") paletaweb-java/" + PaletaWebVersion.VERSION;
	
	/** default expiration for a presigned URL is 7 days in seconds */
	//private static final int DEFAULT_EXPIRY_TIME = SharedConstant.DEFAULT_EXPIRY_TIME;
	  
	private static final String APPLICATION_JSON = "application/json";
	private static final DateTimeFormatter http_date = DateTimeFormatter.RFC_1123_DATE_TIME;	  
	  
	/** private static final String NULL_STRING = "(null)"; */
	private static final String END_HTTP =   "----------END-HTTP----------";
	private static final String START_HTTP = "----------START-HTTP--------";

	private static final String linux_home   = (new File(System.getProperty("user.dir"))).getPath();
	private static final String windows_home = System.getProperty("user.dir");

	/** private static final String UPLOAD_ID = "uploadId";
		the current client instance's base URL. */
	private HttpUrl baseUrl;
	
	
	/** access key to sign all requests with */
	 private String accessKey;

	 /** Secret key to sign all requests with */
	 private String secretKey;
	  
	 private String url;
	 
	 private int port;
	 
	 private String userAgent = DEFAULT_USER_AGENT;

	 private OkHttpClient httpClient;
	  
	 private final OffsetDateTime created = OffsetDateTime.now();
	 //private final ObjectMapper objectMapper = new ObjectMapper();

	 private int chunkSize=0;
	 

	 private String charset = Charset.defaultCharset().name();
	
	 @Autowired
	 private SettingsService paletaWebConfigurationService;
	 
	 
	
	 public PaletaClientService() {
	 }
	
	
	 
	@PostConstruct
	private void initialize() {
	
		this.accessKey  = paletaWebConfigurationService.getAccessKey();
		this.secretKey  = paletaWebConfigurationService.getSecretKey();
			
		this.url  = paletaWebConfigurationService.getUrl();
		this.port =  paletaWebConfigurationService.getPort();
	
		Check.requireNonNullStringArgument(url,  "url is null or emtpy");
		Check.requireNonNullStringArgument(accessKey, "accessKey is null or emtpy");
		Check.requireNonNullStringArgument(secretKey, "secretKey is null or emtpy");
		
		  if (this.port < 0 || this.port > 65535) 
		      throw new IllegalArgumentException("port must be in range of 1 to 65535 -> " + String.valueOf(port));

		  List<Protocol> protocol = new ArrayList<>();
	      protocol.add(Protocol.HTTP_1_1); 

	      /**
	      this.httpClient = new OkHttpClient();
	      
	      File cacheDirectory = new File(getCacheWorkDir());
	      Cache cache = new Cache(cacheDirectory, HTTP_CACHE_SIZE);
	      
	      this.httpClient = this.httpClient.newBuilder()
			        .connectTimeout(DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
			        .writeTimeout(DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
			        .readTimeout(DEFAULT_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
			        .protocols(protocol)
			        .cache(cache)
			        .build();
			      
			    
		   HttpUrl http_url = HttpUrl.parse(this.url);

		   boolean secure = false;
		   
		   if (http_url != null) {

				  if (!"/".equals(http_url.encodedPath())) {
			        	  throw new IllegalArgumentException("no path allowed in endpoint -> " + url);
			      }

		          HttpUrl.Builder urlBuilder = http_url.newBuilder();
		          Scheme scheme = (secure) ? Scheme.HTTPS : Scheme.HTTP;
		          urlBuilder.scheme(scheme.toString());
		          if (port > 0)
		            urlBuilder.port(port);
		          this.baseUrl = urlBuilder.build();
		          return;
		    }

		    if (!this.isValidEndpoint(url))
		      throw new IllegalArgumentException("invalid host -> " +  url);
		    
		    Scheme scheme = (secure) ? Scheme.HTTPS : Scheme.HTTP;

		    if (port == 0) {
		    	this.baseUrl = new HttpUrl.Builder()
		          .scheme(scheme.toString())
		          .host(url)
		          .build();
		    } else {
		    	this.baseUrl = new HttpUrl.Builder()
		          .scheme(scheme.toString())
		          .host(url)
		          .port(port)
		          .build();
		    }
		    */
		    
	  }

		

	private Request createRequest(
				String relativePath[], 
				Method method, 
				Multimap<String,String> headerMap, 
				Multimap<String,String> queryParamMap,
				final String contentType,
				Object body,
				int length,
				boolean multiPart) throws NoSuchAlgorithmException, IOException {

				HttpUrl.Builder urlBuilder = this.baseUrl.newBuilder();
					
				if (relativePath!=null) {
					for (String str: relativePath) 
					urlBuilder.addEncodedPathSegment(str);
				}
					
				if (queryParamMap != null) {
					for (Map.Entry<String,String> entry : queryParamMap.entries()) {
						urlBuilder.addEncodedQueryParameter(entry.getKey(), entry.getValue());
					}
				}
					
				HttpUrl url = urlBuilder.build();
					
				Request.Builder requestBuilder = new Request.Builder();
					
				requestBuilder.url(url);
					
				String sha256 = null;
					
				if (body != null) {
				// body must be byte[] or 
					sha256 = Digest.sha256Hash(body, length);
				}
					
				if (this.accessKey != null && this.secretKey != null) {
						String encoding = Base64.getEncoder().encodeToString((accessKey + ":" + secretKey).getBytes());
						String authHeader = "Basic " + encoding;
						requestBuilder.header("Authorization", authHeader);
				}
					
				if (sha256 != null)
					requestBuilder.header("ETag", sha256);
					
				requestBuilder.header("Host", this.shouldOmitPortInHostHeader(url) ? url.host() : (url.host() + ":" + url.port()));
				requestBuilder.header("User-Agent", this.userAgent);
				requestBuilder.header("Accept", APPLICATION_JSON);
				requestBuilder.header("Accept-Charset", "utf-8");
				requestBuilder.header("Accept-Encoding", "gzip, deflate");
				requestBuilder.header("Date", http_date.format(OffsetDateTime.now()));
				
				if (multiPart) {
					requestBuilder.header("Transfer-Encoding", "gzip, chunked");
				}
					
				if (headerMap != null) {
						for (Map.Entry<String,String> entry : headerMap.entries()) {
							requestBuilder.header(entry.getKey(), entry.getValue());
						}
				}
					
				if (body != null) {
							RequestBody requestBody = null;
							requestBody = new HttpRequestBody(contentType, body, length);
							if (multiPart) {
								String fileName = queryParamMap.get("filename").toString();
								MultipartBody multipartBody = new MultipartBody.Builder()
								.setType(MultipartBody.FORM)  // Header to show we are sending a Multipart Form Data
								.addFormDataPart("file", fileName, requestBody) // file param
								.addFormDataPart("Content-Type", contentType) // other string params can be like userId, name or something
								.build();
								requestBuilder.method(method.toString(), multipartBody);
							}
							else {
								requestBuilder.method(method.toString(), requestBody);
							}
					}
					else {
						requestBuilder.method(method.toString(), null);
					}
					return requestBuilder.build();
	}
	
	
	private HttpResponse executeReq(	String relativePath[], 
										Method method, 
										Multimap<String,String> headerMap, 
										Multimap<String, String> queryParamMap,
										Object body, 
										int length, 
										boolean multiPart)  {
	
		String contentType = null;
		
		if (headerMap != null && headerMap.get("Content-Type") != null) {
			contentType = String.join(" ", headerMap.get("Content-Type"));
		}
		
		if (body != null && !(body instanceof InputStream || body instanceof RandomAccessFile || body instanceof byte[])) {
			byte[] bytes = body.toString().getBytes(StandardCharsets.UTF_8);
			body = bytes;
			length = bytes.length;
		}
		
		Request request = null;
		
		try {
		request = createRequest(relativePath, method, headerMap, queryParamMap, contentType, body, length, multiPart);
		
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		
		if (logger.isDebugEnabled()) {
			logger.debug(START_HTTP);
			String encodedPath = request.url().encodedPath();
			String encodedQuery = request.url().encodedQuery();
			if (encodedQuery != null) {
				encodedPath += "?" + encodedQuery;
		}
			logger.debug(request.method() + " " + encodedPath + " HTTP/1.1");
			String headers = request.headers().toString()
			.replaceAll("Signature=([0-9a-f]+)", "Signature=*REDACTED*")
			.replaceAll("Credential=([^/]+)", "Credential=*REDACTED*");
			logger.debug(headers);
			logger.debug();
		}

		Response response;


				try {
					response = this.httpClient.newCall(request).execute();
				
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				
		
				if (logger.isDebugEnabled()) {
					logger.debug(response.protocol().toString().toUpperCase(Locale.US) + " " + response.code());
					logger.debug(response.headers());
				}
		
				ResponseHeader header = new ResponseHeader();
				HeaderParser.set(response.headers(), header);
		
				if (response.isSuccessful()) {
				
					if (logger.isDebugEnabled()) 
						logger.debug(END_HTTP);
					
					return new HttpResponse(header, response);
				}
		
		/** if response is not successful -> throw OdilonException  ---------------------------- */

				
		
		String str;
		
		try {
		
			str = response.body().string();
			
			if (logger.isDebugEnabled()) {
				logger.debug("error response body -> " + (str!=null?str:"null"));
			}
		
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		int httpCode = response.code();
		
		throw new RuntimeException(String.valueOf(httpCode));
		
		//if (httpCode==ODHttpStatus.UNAUTHORIZED.value()) 
		//	throw new ODClientException(	ODHttpStatus.UNAUTHORIZED.value(), ErrorCode.AUTHENTICATION_ERROR.value(), ErrorCode.AUTHENTICATION_ERROR.getMessage());
		//
		//if (httpCode==ODHttpStatus.INTERNAL_SERVER_ERROR.value()) { 
		//throw new ODClientException(ODHttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.INTERNAL_ERROR.value(), response.toString());
		//}
		//
		//if (httpCode==ODHttpStatus.FORBIDDEN.value()) { 
		//throw new ODClientException(ODHttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.ACCESS_DENIED.value(), response.toString());
		//}
		
		//try {
		//OdilonErrorProxy proxy = this.objectMapper.readValue(str, OdilonErrorProxy.class);
		//ODClientException ex = new ODClientException(proxy.getHttpStatus(), proxy.getErrorCode(), proxy.getMessage());
		//Map<String, String> context = new HashMap<String, String>();
		//context.put( "bucketName",bucketName.orElse("null"));
		//context.put( "objectName",objectName.orElse("null"));
		//
		//if (queryParamMap!=null) {
		//	queryParamMap.asMap().forEach( (k,v) -> context.put(k, v.toString()) );
		//}
		//ex.setContext(context);
		//
		//throw(ex);
		//
		//} catch (JsonProcessingException e) {
		//throw new InternalCriticalException(e, str!=null? str:"");
		//}
}



	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	private String getCacheWorkDir() {									
 		return getHomeDirAbsolutePath() + File.separator + "tmp";
	}
	
	private String getHomeDirAbsolutePath() {
			if (isLinux())
				return linux_home;
			return windows_home;
	}
	  
	 private static boolean isLinux() {
			if  (System.getenv("OS")!=null && System.getenv("OS").toLowerCase().contains("windows")) 
				return false;
			return true;
	}

	 
	 /**
		 * @param endpoint
		 * @return {@code true} if the endpoint is valid {@link https://en.wikipedia.org/wiki/Hostname#Restrictions_on_valid_host_names}
		 * 
		 */
		private boolean isValidEndpoint(String endpoint) {
				
			  Check.requireNonNullStringArgument(endpoint, "endpoint is null or empty");
			  
			  if (InetAddressValidator.getInstance().isValid(endpoint)) {
			      return true;
			    }
			    // endpoint may be a hostname
			    // 
			    if (endpoint.length() < 1 || endpoint.length() > 253) {
			      return false;
			    }
		
			    for (String label : endpoint.split("\\.")) {
			      if (label.length() < 1 || label.length() > 63) {
			        return false;
			      }
		
			      if (!(label.matches(SharedConstant.valid_endpoint_regex))) {
			        return false;
			      }
			    }
			    return true;
		}
		
		  /**
		   * <p>Checks whether port should be omitted in Host header.
		   * HTTP Spec (rfc2616) defines that port should be omitted in Host header
		   * when port and service matches (i.e HTTP -> 80, HTTPS -> 443)
		   *</p>
		   * @param url Url object
		   */
		  private boolean shouldOmitPortInHostHeader(HttpUrl url) {
		    return (url.scheme().equals("http") && url.port() == 80)
		      || (url.scheme().equals("https") && url.port() == 443);
		  }
		  

	 
	
}
