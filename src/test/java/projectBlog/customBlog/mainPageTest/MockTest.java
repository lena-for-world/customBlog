package projectBlog.customBlog.mainPageTest;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class MockTest {

    @Autowired
    WebApplicationContext wac;
    MockMvc mockMVC;

    @BeforeEach
    public void init() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac)
            .alwaysExpect(status().isOk())
            .build();
    }

    @Test
    public void test() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/");
        mockMVC.perform(requestBuilder)
            .andExpect(handler().methodName("home"))
            .andDo(print())
            .andExpect(status().isOk());
    }

}
