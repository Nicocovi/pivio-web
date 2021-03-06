package io.pivio.view;

import io.pivio.view.app.feed.serverresponse.FeedItem;
import io.pivio.view.app.overview.detail.ServiceIdShortName;
import io.pivio.view.app.overview.detail.serverresponse.Document;
import io.pivio.view.app.overview.list.serverresponse.Overview;
import io.pivio.view.configuration.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;

import static java.util.Collections.sort;

@Component
public class PivioServerConnector {

    private final Logger log = LoggerFactory.getLogger(PivioServerConnector.class);

    @Autowired
    ServerConfig serverConfig;

    public <T> ResponseEntity query(String path, String query, Class<T> type) throws UnsupportedEncodingException {
        String encodedQuery = URLEncoder.encode(query, "UTF-8");
        return get(serverConfig.apiAddress + path + encodedQuery, type);
    }

    //change aufruf f�r sql
    public Document getDocumentById(String id) throws UnsupportedEncodingException {
        ResponseEntity responseEntity = get(serverConfig.apiAddress + "/document/" + id, Document.class);
        return (Document) responseEntity.getBody();
    }

    public <T> ResponseEntity list(String path, Class<T> type) throws UnsupportedEncodingException {
        return get(serverConfig.apiAddress + path, type);
    }

    //change aufruf f�r sql
    public List<Overview> getOverviews() throws IOException {
    	//matchAllQuery (Nico)
    	String matchAllQuery = "&query={\"query\":{\"match_all\":{}}}";
    	String encodedQuery = URLEncoder.encode(matchAllQuery, "UTF-8");
        String path = "/document?"+encodedQuery; //hier
        String url = serverConfig.apiAddress + path;
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<List<Overview>> typeRef = new ParameterizedTypeReference<List<Overview>>() {
        };
        List<Overview> result;
        try {
            ResponseEntity<List<Overview>> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>("", getHeaders()), typeRef);
            result = response.getBody();
            sort(result);
        } catch (Exception e) {
            log.error("Pivio Server at {} does not respond (Exception=\n{}\n).", url, e.getMessage());
            throw new IOException("Unable to connect to " + url + ".");
        }
        return result;
    }

    private <T> ResponseEntity get(String url, Class<T> type) throws UnsupportedEncodingException {
        RestTemplate restTemplate = new RestTemplate();
        log.debug("Asking for :" + url);
        ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>("", getHeaders()), type);
        log.debug("Response :" + response.getBody().toString());
        return response;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

    //change aufruf f�r sql
    public List<ServiceIdShortName> getAllServices() {
        String url = serverConfig.apiAddress + "/document?fields=service,id,short_name"; //hier
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<List<ServiceIdShortName>> typeRef = new ParameterizedTypeReference<List<ServiceIdShortName>>() {
        };
        log.debug("Query {}.", url);
        ResponseEntity<List<ServiceIdShortName>> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>("", getHeaders()), typeRef);
        return response.getBody();
    }
    
    //change aufruf f�r sql
    public List<String> getAllNames() throws IOException{
        String url = serverConfig.apiAddress + "/document?fields=name"; //+encodedQuery; //hier
        RestTemplate restTemplate = new RestTemplate();
        ParameterizedTypeReference<List<Document>> typeRef = new ParameterizedTypeReference<List<Document>>() {
        };
        log.debug("Query {}.", url);
        List<Document> result;
        List<String> nameList = new ArrayList<>();
        try {
        	ResponseEntity<List<Document>> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>("", getHeaders()), typeRef);
        	result = response.getBody();
        	result.forEach(r->
        		nameList.add(r.name)
        	);
        }catch(Exception e) {
        	log.error("Pivio Server at {} does not respond (Exception=\n{}\n).", url, e.getMessage());
            throw new IOException("Unable to connect to " + url + ".");
        }
        return nameList;
    }

    public boolean deleteDocument(String id) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        String url = serverConfig.apiAddress + "/document/" + id;
        ResponseEntity<Object> exchange = null;
        try {
            exchange = restTemplate.exchange(url, HttpMethod.DELETE, new HttpEntity<>("", headers), Object.class);
        } catch (Exception e) {
            throw new IOException();
        }
        return exchange != null && exchange.getStatusCode() == HttpStatus.ACCEPTED;
    }

    public List<FeedItem> getChangeset() {
        String url = serverConfig.apiAddress + "/changeset?since=7d";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = getHeaders();
        ParameterizedTypeReference<List<FeedItem>> typeRef = new ParameterizedTypeReference<List<FeedItem>>() {
        };
        ResponseEntity<List<FeedItem>> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>("", headers), typeRef);
        return response.getBody();
    }
}
