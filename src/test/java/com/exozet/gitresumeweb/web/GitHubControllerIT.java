package com.exozet.gitresumeweb.web;

import com.exozet.gitresumeweb.GitResumeWebApplication;
import com.exozet.gitresumeweb.service.GitHubClient;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.jayway.restassured.RestAssured;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = GitResumeWebApplication.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Ignore("was a test of a test")
public class GitHubControllerIT {

    @Value("${app.github.host}")
    private String gitHubHost;

    @LocalServerPort
    int serverPort;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(48145);

    MockRestServiceServer mockServer;

    @Value("${server.servlet.context-path}")
    protected String contextPath;

    HttpHeaders defaultHeader;

    @Before
    public void setUp() throws Exception {
        defaultHeader = new HttpHeaders();
        defaultHeader.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        defaultHeader.setAccept(Lists.newArrayList(MediaType.TEXT_HTML));

        RestTemplateBuilder templateBuilder =
                new RestTemplateBuilder().
                        rootUri(testRestTemplate.getRootUri())
                        .messageConverters(new FormHttpMessageConverter(), new StringHttpMessageConverter(), new ByteArrayHttpMessageConverter());


        testRestTemplate = new TestRestTemplate(templateBuilder, null, null, TestRestTemplate.HttpClientOption.ENABLE_COOKIES);
       
    }

    @Test
    public void showUserProfile() throws IOException {
        WireMock.get(urlMatching(gitHubHost + GitHubClient.API_PATH_USERS.replace("{user}","MOREMIMO")))
                .withQueryParam("username", WireMock.equalTo("MOREMIMO"))
                .willReturn( aResponse().withStatus(200).withHeader("Content-Type", "text/xml; charset=UTF-8")
                        .withBody(getResource("gitmocks/get_user.json"))
        );
        WireMock.get(urlMatching(gitHubHost + GitHubClient.API_PATH_REPOS.replace("{user}","MOREMIMO")))
                .withQueryParam("username", WireMock.equalTo("MOREMIMO"))
                .willReturn( aResponse().withStatus(200).withHeader("Content-Type", "text/xml; charset=UTF-8")
                        .withBody(getResource("gitmocks/get_repos.json"))
                );





        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("username", "MOREMIMO");
        map.add("save", "submit");


        HttpEntity<MultiValueMap<String, String>> requestEntity=
                new HttpEntity<MultiValueMap<String, String>>(map, defaultHeader);
        

        ResponseEntity<String> response = testRestTemplate.postForEntity("/users/profile",requestEntity, String.class, Collections.emptyList());



        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusCodeValue());
               
    }

    private String getResource(String path) throws IOException {
        return Resources.toString(Resources.getResource(path), Charsets.UTF_8);
    }
}