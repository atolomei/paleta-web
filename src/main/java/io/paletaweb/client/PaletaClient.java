package io.paletaweb.client;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.paletaweb.SystemService;
import jakarta.annotation.PostConstruct;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;


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
	  
	 private String userAgent = DEFAULT_USER_AGENT;

	 private OkHttpClient httpClient;
	  
	 private final OffsetDateTime created = OffsetDateTime.now();
	 //private final ObjectMapper objectMapper = new ObjectMapper();

	 private int chunkSize=0;
	 
	 private boolean isLogStream = false;

	 private String charset = Charset.defaultCharset().name();
	
	 
	 
	public  PaletaClient() {
		
	}
	
	
	
	@PostConstruct
	private void initialize() {
		// connect to Paleta Server
	}

	
	
	
	 
	
}
