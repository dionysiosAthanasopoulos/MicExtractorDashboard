package httpClients;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;


public class AwsHttpPost {

	public static String execute(String url, String payload, String apiKey, String accessKey, String secretAccessKey) throws Exception {

		String restResponse = "";

		PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();

		cm.setMaxTotal(306);

		cm.setDefaultMaxPerRoute(108);

		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout((int) Math.pow(10, 10)).setSocketTimeout((int) Math.pow(10, 10)).build();

		CloseableHttpClient client = HttpClients.custom().setConnectionManager(cm).setDefaultRequestConfig(requestConfig).build();


		CloseableHttpResponse response = null;

		HttpPost post = new HttpPost(url);

		HashMap<String, String> headers = buildAuthHeaders("POST", url, payload, "application/json", "eu-west-1", "execute-api", apiKey, accessKey, secretAccessKey); //System.out.println(headers);

		Header[] headersArray = new Header[headers.size()];

		int i = -1;

		for(String header: headers.keySet()) headersArray[++i] = new BasicHeader(header, headers.get(header));

		post.setHeaders(headersArray);


		StringEntity input = new StringEntity(payload);

		input.setContentType("application/json");

		post.setEntity(input);

		response = client.execute(post);


		int linesNum = 0;

		if(response.getEntity() != null && response.getEntity().getContent() != null) {

			BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

			String line = null;

			while ((line = rd.readLine()) != null) {

				++linesNum;

				restResponse += line;
			}

			rd.close();
		}

		client.close();

		if(linesNum == 0) restResponse = null;

		return restResponse;
	}

	private static HashMap<String, String> buildAuthHeaders(String method, String path, String body, String mediaType, String awsRegion, String awsService, String apiKey, String accessKey, String secretAccessKey) throws Exception {

		HashMap<String, String> finalHeaders = new HashMap<String, String>();

		HashMap<String, String> headers = new HashMap<String, String>();

		StringBuilder canonicalRequestSb = new StringBuilder();

		appendOptionalString(canonicalRequestSb, method);

		URL endpointUrl = new URI(path).toURL(); //System.out.println(endpointUrl.toString());

		appendOptionalString(canonicalRequestSb, path.substring(path.indexOf(endpointUrl.getHost()) + endpointUrl.getHost().length()));

		canonicalRequestSb.append('\n');


		String hostHeader = "Host", dateHeader = "X-Amz-Date", apiKeyHeader = "X-Api-Key", accept = "Accept", contentType = "Content-Type", contentLength = "Content-Length", contentHashHeader = "X-Amz-Content-Sha256";

        List<String> headerList = new ArrayList<String>(Arrays.asList(hostHeader, accept, apiKeyHeader, contentType, contentLength, contentHashHeader, dateHeader));

        Collections.sort(headerList, String.CASE_INSENSITIVE_ORDER);

        StringBuilder headersLower = new StringBuilder();

        String contentHashHeaderValue = hex(sha256(body.getBytes(StandardCharsets.UTF_8)));

        for(String header: headerList){

        	String value = null;

            if(header.equals(hostHeader)) value = endpointUrl.getHost();

            else if(header.equals(apiKeyHeader)) value = apiKey;

            else if(header.equals(contentHashHeader) || header.equals(accept)) value = "";

            else if(header.equals(contentType)) value = mediaType;

            else if(header.equals(contentLength)) value = String.valueOf(body.getBytes(StandardCharsets.UTF_8).length);

            else if(header.equals(dateHeader)) value = generateDate();

            if(value != null) {

	            headers.put(header, value);
	
	            String headerLower = header.toLowerCase();
	
	            if(headersLower.length() > 0) headersLower.append(';');

	            headersLower.append(headerLower);

	            String cannonicalValue = headerLower + ":" + value.trim();

	           	appendOptionalString(canonicalRequestSb, cannonicalValue);
            }
        }

        canonicalRequestSb.append('\n');

        String signedHeaders = headersLower.toString();

        appendOptionalString(canonicalRequestSb, signedHeaders);

        appendOptionalString(canonicalRequestSb, contentHashHeaderValue);

        canonicalRequestSb.setLength(canonicalRequestSb.length()-1); //System.out.println("canonicalRequestSb = \n" + canonicalRequestSb);

        String canonicalRequestHash = hex(sha256(canonicalRequestSb.toString().getBytes(StandardCharsets.UTF_8)));


        String encryptionAlgorithm = "AWS4-HMAC-SHA256";

        StringBuilder stringToSignSb = new StringBuilder();

        stringToSignSb.append(encryptionAlgorithm).append('\n');

        String dateStr = headers.get(dateHeader);

        stringToSignSb.append(dateStr).append('\n');

        String requestType = "aws4_request";

        String credentialScope = dateStr.substring(0, 8) + "/" + awsRegion + "/" + awsService + "/" + requestType;

        stringToSignSb.append(credentialScope).append('\n');

        stringToSignSb.append(canonicalRequestHash);

        String stringToSign = stringToSignSb.toString(); //System.out.println("\nstringToSign = \n" + stringToSign);


        byte[] keySecret = ("AWS4" + secretAccessKey).getBytes(StandardCharsets.UTF_8);

        byte[] keyDate = hmac(keySecret, dateStr.substring(0, 8));

        byte[] keyRegion = hmac(keyDate, awsRegion);

        byte[] keyService = hmac(keyRegion, awsService);

        byte[] keySigning = hmac(keyService, requestType);

        String signature = hex(hmac(keySigning, stringToSign));


        String authParameter = encryptionAlgorithm + " Credential=" + accessKey + "/" + credentialScope + ", SignedHeaders=" + signedHeaders + ", Signature=" + signature;

        finalHeaders.put(dateHeader, headers.get(dateHeader));

        finalHeaders.put(apiKeyHeader, apiKey);

        finalHeaders.put("Authorization", authParameter);

        return finalHeaders;
	}

	private static void appendOptionalString(StringBuilder sb, String str) { if(str != null) sb.append(str + '\n'); }

	private static String hex(byte[] bytes) {

		final String HEX_CHARACTERS = "0123456789abcdef";

		StringBuilder sb = new StringBuilder(bytes.length * 2);

		for(int i = 0; i < bytes.length; i++) {

			int val = (bytes[i] & 0xFF);

			sb.append(HEX_CHARACTERS.charAt(val >>> 4));

			sb.append(HEX_CHARACTERS.charAt(val & 0x0F));
		}

		return sb.toString();
	}

	private static byte[] sha256(byte[] bytes) throws Exception{

		MessageDigest digest = MessageDigest.getInstance("SHA-256");

		digest.update(bytes);

		return digest.digest();
	}

	private static byte[] hmac(byte[] key, String input) throws Exception {

		Mac mac = Mac.getInstance("HmacSHA256");

		mac.init(new SecretKeySpec(key, "HmacSHA256"));

		return mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
	}

	public static String generateDate() throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");

		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

		return sdf.format(new Date());
	}
}