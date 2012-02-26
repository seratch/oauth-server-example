package example;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.sun.jersey.oauth.client.OAuthClientFilter;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import org.junit.Test;

import javax.ws.rs.core.MultivaluedMap;
import java.net.URI;

public class OAuthTest {

    @Test
    public void get_200() throws Exception {

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource resource = client.resource(new URI("http://localhost:8080/"));

        OAuthParameters params = new OAuthParameters();
        params.signatureMethod("HMAC-SHA1").consumerKey("CONSUMER_KEY").setToken("TOKEN");
        OAuthSecrets secrets = new OAuthSecrets();
        secrets.consumerSecret("CONSUMER_SECRET").setTokenSecret("TOKEN_SECRET");
        OAuthClientFilter filter = new OAuthClientFilter(client.getProviders(), params, secrets);
        resource.addFilter(filter);
        System.out.println(resource.get(String.class));
    }

    @Test(expected = UniformInterfaceException.class)
    public void get_401() throws Exception {

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource resource = client.resource(new URI("http://localhost:8080/"));

        OAuthParameters params = new OAuthParameters();
        params.signatureMethod("HMAC-SHA1").consumerKey("CONSUMER_KEY").setToken("TOKEN");
        OAuthSecrets secrets = new OAuthSecrets();
        secrets.consumerSecret("CONSUMER_SECRETXXX").setTokenSecret("TOKEN_SECRET");
        OAuthClientFilter filter = new OAuthClientFilter(client.getProviders(), params, secrets);
        resource.addFilter(filter);
        System.out.println(resource.get(String.class));
    }

    @Test
    public void post_200() throws Exception {

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource resource = client.resource(new URI("http://localhost:8080/post/submit"));

        OAuthParameters params = new OAuthParameters();
        params.signatureMethod("HMAC-SHA1").consumerKey("CONSUMER_KEY").setToken("TOKEN");
        OAuthSecrets secrets = new OAuthSecrets();
        secrets.consumerSecret("CONSUMER_SECRET").setTokenSecret("TOKEN_SECRET");
        OAuthClientFilter filter = new OAuthClientFilter(client.getProviders(), params, secrets);
        resource.addFilter(filter);

        MultivaluedMap formParams = new MultivaluedMapImpl();
        formParams.add("id", "foo");
        formParams.add("password", "bar");
        System.out.println(resource.type("application/x-www-form-urlencoded").post(String.class, formParams));
    }

    @Test(expected = UniformInterfaceException.class)
    public void post_401() throws Exception {

        ClientConfig config = new DefaultClientConfig();
        Client client = Client.create(config);
        WebResource resource = client.resource(new URI("http://localhost:8080/post/submit"));

        OAuthParameters params = new OAuthParameters();
        params.signatureMethod("HMAC-SHA1").consumerKey("CONSUMER_KEY").setToken("TOKEN");
        OAuthSecrets secrets = new OAuthSecrets();
        secrets.consumerSecret("CONSUMER_SECRETXXX").setTokenSecret("TOKEN_SECRET");
        OAuthClientFilter filter = new OAuthClientFilter(client.getProviders(), params, secrets);
        resource.addFilter(filter);

        MultivaluedMap formParams = new MultivaluedMapImpl();
        formParams.add("id", "foo");
        formParams.add("password", "bar");
        System.out.println(resource.type("application/x-www-form-urlencoded").post(String.class, formParams));
    }


}
