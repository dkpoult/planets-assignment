package za.co.dinoko.assignment.davidPoulton.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import za.co.dinoko.assignment.davidPoulton.api.RouteRequest;
import za.co.dinoko.assignment.davidPoulton.db.RouteRepository;
import za.co.dinoko.assignment.davidPoulton.db.models.Route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RoutingTests {

    private ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testBasicRoute() throws Exception {
        RouteRequest request = new RouteRequest("A", "B");

        String content = mapper.writeValueAsString(request);

        JSONObject response = new JSONObject(mockMvc.perform(get("/routing")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andReturn()
                .getResponse()
                .getContentAsString()
        );

        JSONArray nodes = response.getJSONArray("planetNodes");
        JSONArray trueNodes = new JSONArray();
        trueNodes.put("A");
        trueNodes.put("B");

        assertThat(nodes).isEqualTo(trueNodes);
    }

    @Test
    void testHardRoute() throws Exception {
        RouteRequest request = new RouteRequest("A", "C'");

        String content = mapper.writeValueAsString(request);

        JSONObject response = new JSONObject(mockMvc.perform(get("/routing")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andReturn()
                .getResponse()
                .getContentAsString()
        );

        JSONArray nodes = response.getJSONArray("planetNodes");
        JSONArray trueNodes = new JSONArray();
        trueNodes.put("A");
        trueNodes.put("C");
        trueNodes.put("F");
        trueNodes.put("J");
        trueNodes.put("R");
        trueNodes.put("P");
        trueNodes.put("U");
        trueNodes.put("K'");
        trueNodes.put("W");
        trueNodes.put("C'");

        assertThat(nodes).isEqualTo(trueNodes);
    }

    @Test
    void testImpossibleRoute() throws Exception {
        RouteRequest request = new RouteRequest("A", "FAKE");

        String content = mapper.writeValueAsString(request);

        mockMvc.perform(get("/routing")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest());
    }

}
