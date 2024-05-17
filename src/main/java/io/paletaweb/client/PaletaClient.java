package io.paletaweb.client;


import java.io.File;
import java.nio.charset.Charset;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import io.paleta.model.SharedConstant;
import io.paleta.util.Check;
import io.paletaweb.PaletaWebConfigurationService;
import io.paletaweb.SystemService;
import jakarta.annotation.PostConstruct;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;


@Service
public class PaletaClient implements SystemService {

	private static final int BUFFER_SIZE = 8192;
	
	/** 
	 * 	MONITOR  

	private static final String API_PING 									[] = {"ping"};
	private static final String API_METRICS									[] = {"metrics"};
	private static final String API_SYSTEM_INFO								[] = {"systeminfo"};
	 */	
	/** 
	 * 	BUCKET 

	private static final String API_BUCKET_LIST 							[] = {"bucket", "list"};
	private static final String API_BUCKET_GET	 							[] = {"bucket", "get"};
	private static final String API_BUCKET_EXISTS 							[] = {"bucket", "exists"};
	private static final String API_BUCKET_CREATE	 						[] = {"bucket", "create"};
	private static final String API_BUCKET_DELETE	 						[] = {"bucket", "delete"};
	private static final String API_BUCKET_ISEMPTY							[] = {"bucket", "isempty"};
	private static final String API_BUCKET_LIST_OBJECTS						[] = {"bucket", "objects"};
								
	private static final String API_BUCKET_DELETE_ALL_PREVIOUS_VERSION		[] = {"bucket", "deleteallpreviousversion"};
	 */	
	
	
	/** ---------------------------------------------------- */
	
	private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
	private static final int HTTP_CACHE_SIZE = 200 * 1024 * 1024; // 200 mb
	
    /** default network I/O timeout is 15 minutes */
	public static final int DEFAULT_CONNECTION_TIMEOUT = 15 * 60;
	public static final String DEFAULT_USER_AGENT = "PaletaWeb (" + System.getProperty("os.arch") + "; " + System.getProperty("os.arch") + ") paletaweb-java/" + 
	"#OdilonClientProperties.INSTANCE.getVersion()";
	
	/** default expiration for a presigned URL is 7 days in seconds */
	//private static final int DEFAULT_EXPIRY_TIME = SharedConstant.DEFAULT_EXPIRY_TIME;
	  
	private static final String APPLICATION_JSON = "application/json";
	private static final DateTimeFormatter http_date = DateTimeFormatter.RFC_1123_DATE_TIME;	  
	  
	/** private static final String NULL_STRING = "(null)"; */
	private static final String END_HTTP = "----------END-HTTP----------";

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
	 private PaletaWebConfigurationService paletaWebConfigurationService;
	 
	 
	public  PaletaClient() {
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
	 
	
}
