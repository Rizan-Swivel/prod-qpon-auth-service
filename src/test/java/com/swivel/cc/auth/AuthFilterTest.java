//package com.swivel.cc.auth;
//
//import com.swivel.cc.auth.configuration.Translator;
//import com.swivel.cc.auth.util.FilterErrorResponseGenerator;
//import com.swivel.cc.auth.util.Validator;
//import com.swivel.cc.auth.configuration.AuthFilter;
//import lombok.extern.slf4j.Slf4j;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.http.HttpStatus;
//import org.springframework.mock.web.MockFilterChain;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.mock.web.MockHttpServletResponse;
//
//import javax.servlet.ServletException;
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.MockitoAnnotations.initMocks;
//
//@AutoConfigureMockMvc
//@Slf4j
//public class AuthFilterTest {
//
//    private final Validator validator = new Validator();
//    @Mock
//    private Translator translator;
//    @Mock
//    private AuthFilter filter;
//
//    @BeforeEach
//    void setUp() {
//        initMocks(this);
//        FilterErrorResponseGenerator errorResponseGenerator = new FilterErrorResponseGenerator(translator);
//        this.filter = new AuthFilter(validator, errorResponseGenerator);
//    }
//
//    @Test
//    void Should_ThrowBadRequest_When_Required_Parameters_Not_Provided() throws IOException, ServletException {
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        MockFilterChain filterChain = new MockFilterChain();
//
//        filter.doFilter(request, response, filterChain);
//
//        try {
//            JSONObject jsonObject = new JSONObject(response.getContentAsString());
//            assertEquals("null", jsonObject.getString("data"));
//            assertEquals("Required fields are missing", jsonObject.getString("message"));
//            assertEquals(4000, jsonObject.getInt("errorCode"));
//        } catch (JSONException err) {
//            log.error("Error occurred while json conversion " + err);
//        }
//        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
//
//    }
//
//    @Test
//    void Should_ThrowBadRequest_When_Invalid_Inputs() throws IOException, ServletException {
//
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        MockFilterChain filterChain = new MockFilterChain();
//
//        request.setParameter("username", "notvalid");
//        request.setParameter("password", "password");
//
//        filter.doFilter(request, response, filterChain);
//
//        try {
//            JSONObject jsonObject = new JSONObject(response.getContentAsString());
//            assertEquals("null", jsonObject.getString("data"));
//            assertEquals("Invalid username or password", jsonObject.getString("message"));
//            assertEquals(4105, jsonObject.getInt("errorCode"));
//        } catch (JSONException err) {
//            log.error("Error occurred while json conversion " + err);
//        }
//        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST.value());
//
//    }
//
//}
