package me.youlfey.rest.example.books.portal.controller.xslt;

import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.URI;
import java.util.Objects;
import java.util.Optional;

@RequestMapping(value = "/xslt")
@RestController
public class GetController {
    @Setter(onMethod = @__(@Autowired))
    private Environment environment;
    private RestTemplate rest;
    private DocumentBuilder documentBuilder;

    @Bean
    public RestTemplate restTemplate() {
        return (rest = new RestTemplate());
    }

    @SneakyThrows
    @Bean
    public DocumentBuilder documentBuilder() {
        return (documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder());
    }

    @SneakyThrows
    private URI buildUri(String staticUri) {
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = environment.getProperty("server.port");
        return URI.create(String.format("http://%s:%s%s", host, port, staticUri));
    }
    private String getStaticUri(HttpServletRequest request) {
        return request.getRequestURI().replaceAll("/xslt", "");
    }

    @GetMapping(value = "/writers", produces = MediaType.APPLICATION_XML_VALUE)
    private ResponseEntity<String> getWriters(HttpServletRequest request) {
        String uri = getStaticUri(request);
        ResponseEntity<String> responseEntity = getResponse(uri);
        Document document = buildDocument(Objects.requireNonNull(responseEntity.getBody()));
        return ResponseEntity.of(Optional.of(xmlToString(document, uri)));
    }

    @GetMapping(value = "/books", produces = MediaType.APPLICATION_XML_VALUE)
    private ResponseEntity<String> getBooks(HttpServletRequest request) {
        String uri = getStaticUri(request);
        ResponseEntity<String> responseEntity = getResponse(uri);
        Document document = buildDocument(Objects.requireNonNull(responseEntity.getBody()));
        return ResponseEntity.of(Optional.of(xmlToString(document, uri)));
    }

    private ResponseEntity<String> getResponse(String uri) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", MediaType.APPLICATION_XML_VALUE);
        HttpEntity httpEntity = new HttpEntity<>(null, httpHeaders);
        return rest.exchange(buildUri(uri), HttpMethod.GET, httpEntity, String.class);
    }

    @SneakyThrows
    public Document buildDocument(String elem) {
        try {
            return documentBuilder.parse(new ByteArrayInputStream(elem.getBytes()));
        } catch (SAXException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    private String xmlToString(Document document, String uriStatic) {
        ProcessingInstruction i = document.createProcessingInstruction("xml-stylesheet", String.format("type=\"text/xsl\" href=\"%s.xslt\"", uriStatic));
        document.insertBefore(i, document.getDocumentElement());
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        StringWriter writer = new StringWriter();
        transformer.transform(new DOMSource(document), new StreamResult(writer));
        return writer.getBuffer().toString().replaceAll("\n|\r", "");
    }

}
