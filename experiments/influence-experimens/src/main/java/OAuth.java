import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Properties;
import java.util.Scanner;


import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;


import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;



public class OAuth 
{
	public static void main(String[] args) throws IOException
	{
		
		FileOutputStream f_out = new FileOutputStream(".token");
		ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
		obj_out.writeObject ( accessToken );
		
		
		OAuthService service = new ServiceBuilder()
       			.provider(TwitterApi.class)
        		.apiKey("79K2mIJG1k0DLZ6XGq0qZw")
        		.apiSecret("MycDaSmFOe8iUkdEZhQz7MclHijpaGayjBEzRMx7M")
        		.callback("http://localhost:8080/oauth-app/callback.jsp")
        		.build();
		
		org.scribe.model.Token requestToken = service.getRequestToken();
		String authUrl = service.getAuthorizationUrl(requestToken);		
		System.out.println(authUrl);
		
		Scanner scanner = new Scanner(System.in);
		
		new RequestToken(token, tokenSecret).g
		
		String verifierString = "";
		verifierString = scanner.nextLine();
		Verifier verifier = new Verifier(verifierString);
		org.scribe.model.Token accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println(accessToken);
		

		
//		
//		FileOutputStream fileOutputStream = new FileOutputStream("/home/godzy/token.data");
//		ObjectOutputStream objectOutputStream = new ObjectOutputStream (fileOutputStream);
//		objectOutputStream.writeObject(requestToken);
//		objectOutputStream.close();

	
//		Properties properties = new Properties();
//		properties.setProperty("consumer.key", "79K2mIJG1k0DLZ6XGq0qZw");
//		properties.setProperty("consumer.secret", "MycDaSmFOe8iUkdEZhQz7MclHijpaGayjBEzRMx7M");
//		properties.setProperty("request.token.verb", "POST");
//		properties.setProperty("consumer.key", "http://twitter.com/oauth/request_token");
//		properties.setProperty("access.token.verb", "POST");
//		properties.setProperty("access.token.url", "http://twitter.com/oauth/access_token");
////		properties.setProperty("callback.url", "http://twitter.com/oauth/access_token");
//
//		Scribe scribe = new Scribe(properties );
//		Token accessToken = scribe.getAccessToken(requestToken, "Verifier")
		
//		service
//		
//		Twitter twitter = new TwitterFactory().getInstance();
//		twitter.setOAuthConsumer("79K2mIJG1k0DLZ6XGq0qZw" , "MycDaSmFOe8iUkdEZhQz7MclHijpaGayjBEzRMx7M");
	}
}
