package hu.fnzsoft.atm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = AtmApplication.class)
@AutoConfigureMockMvc
public class AtmIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void givenDepositMoney_whenGetInfo_thenStatus200()
            throws Exception {

        mvc.perform(post("/api/Deposit").contentType(MediaType.APPLICATION_JSON).content(toJson(Map.of("10000",5,"20000",2))))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", is(90000)));
    }

    @Test
    public void givenWithdrawMoney_whenGetInfo_thenStatus200()
            throws Exception {
        mvc.perform(post("/api/Deposit").contentType(MediaType.APPLICATION_JSON).content(toJson(Map.of("10000",1,"20000",0))));
        mvc.perform(post("/api/Withdrawal").contentType(MediaType.APPLICATION_JSON).content(toJson(10000L)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("10000", is(1)));
    }


    static byte[] toJson(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }


}
