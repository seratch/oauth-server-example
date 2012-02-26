package example.resource;

import com.sun.jersey.api.core.HttpContext;
import com.sun.jersey.api.core.HttpRequestContext;
import com.sun.jersey.oauth.server.OAuthServerRequest;
import com.sun.jersey.oauth.signature.OAuthParameters;
import com.sun.jersey.oauth.signature.OAuthSecrets;
import com.sun.jersey.oauth.signature.OAuthSignature;
import com.sun.jersey.oauth.signature.OAuthSignatureException;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Component
@Path("/")
public class OAuthResource {

    static Logger log = LoggerFactory.getLogger(OAuthResource.class);

    @Context
    HttpContext context;

    @GET
    @Path("/")
    public Object index() throws Exception {
        verifyOAuth(context.getRequest());
        return "Hello, OAuth Verified JAX-RS(Jersey) with Spring!";
    }

    @GET
    @Path("/2legged/")
    public Object index2Legged() throws Exception {
        verify2LeggedOAuth(context.getRequest());
        return "Hello, OAuth Verified JAX-RS(Jersey) with Spring!";
    }

    @Resource
    Validator validator;

    @POST
    @Path("/post/submit")
    public Object postSubmit(@FormParam("id") String id,
                             @FormParam("password") String password,
                             MultivaluedMap<String, String> formParams) throws Exception {

        // bug??
        context.getRequest().getQueryParameters().putAll(formParams);
        verifyOAuth(context.getRequest());

        log.info(formParams.toString());

        PostSubmitParams params = new PostSubmitParams(id, password);
        Set<ConstraintViolation<PostSubmitParams>> violations = validator.validate(params);
        if (!violations.isEmpty()) {
            log.debug("Validation failed : " + violations.size());
            for (ConstraintViolation<PostSubmitParams> v : violations) {
                log.debug(v.getPropertyPath().toString() + " " + v.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return "Posted: id=" + id + ",password=" + password;
    }

    @POST
    @Path("/2legged/post/submit")
    public Object postSubmit2Legged(@FormParam("id") String id,
                                    @FormParam("password") String password,
                                    MultivaluedMap<String, String> formParams) throws Exception {

        // bug??
        context.getRequest().getQueryParameters().putAll(formParams);
        verify2LeggedOAuth(context.getRequest());

        PostSubmitParams params = new PostSubmitParams(id, password);
        Set<ConstraintViolation<PostSubmitParams>> violations = validator.validate(params);
        if (!violations.isEmpty()) {
            log.debug("Validation failed : " + violations.size());
            for (ConstraintViolation<PostSubmitParams> v : violations) {
                log.debug(v.getPropertyPath().toString() + " " + v.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        return "Posted: id=" + id + ",password=" + password;
    }


    public static class PostSubmitParams {
        public PostSubmitParams(String id, String password) {
            this.id = id;
            this.password = password;
        }

        @NotEmpty
        public String id;
        @NotEmpty
        public String password;
    }

    static void verifyOAuth(HttpRequestContext context) throws OAuthSignatureException {
        log.info("Authorization: " + context.getHeaderValue("Authorization"));
        try {
            OAuthServerRequest oauthReq = new OAuthServerRequest(context);

            for (String key : context.getFormParameters().keySet()) {
                List<String> value = context.getFormParameters().get(key);
                System.out.println(key + " -> " + value);
            }
            for (String key : oauthReq.getParameterNames()) {
                List<String> value = oauthReq.getHeaderValues(key);
                System.out.println(key + " -> " + value);
            }

            OAuthParameters params = new OAuthParameters();
            params.readRequest(oauthReq);
            OAuthSecrets secrets = new OAuthSecrets().consumerSecret("CONSUMER_SECRET").tokenSecret("TOKEN_SECRET");
            boolean isValid = OAuthSignature.verify(oauthReq, params, secrets);
            if (!isValid) {
                throw new WebApplicationException(401);
            }
        } catch (OAuthSignatureException e) {
            log.info("OAuthSignatureException is thrown!", e);
            throw new WebApplicationException(401);
        }
    }

    static void verify2LeggedOAuth(HttpRequestContext context) throws OAuthSignatureException {
        log.info("Authorization: " + context.getHeaderValue("Authorization"));
        try {
            OAuthServerRequest oauthReq = new OAuthServerRequest(context);
            OAuthParameters params = new OAuthParameters();
            params.readRequest(oauthReq);
            OAuthSecrets secrets = new OAuthSecrets().consumerSecret("CONSUMER_SECRET");
            boolean isValid = OAuthSignature.verify(oauthReq, params, secrets);
            if (!isValid) {
                throw new WebApplicationException(401);
            }
        } catch (OAuthSignatureException e) {
            log.info("OAuthSignatureException is thrown!", e);
            throw new WebApplicationException(401);
        }
    }


}
