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
import za.co.dinoko.assignment.davidPoulton.db.PlanetRepository;
import za.co.dinoko.assignment.davidPoulton.db.RouteRepository;
import za.co.dinoko.assignment.davidPoulton.db.models.Planet;
import za.co.dinoko.assignment.davidPoulton.db.models.Route;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RouteTests {

    private ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RouteRepository routeRepository;

    @Test
    void testGetAllRoutes() throws Exception {
        // This test is very coarse, might be better to check more detailed information about each route
        // but we can trust the per-route test.
        JSONArray response = new JSONArray(mockMvc.perform(get("/routes"))
                .andReturn()
                .getResponse()
                .getContentAsString()
        );

        assertThat(response.length()).isEqualTo(routeRepository.findAll().size());
    }

    @Test
    void testGetSingleRoute() throws Exception {
        JSONObject response = new JSONObject(mockMvc.perform(get("/route/1"))
                .andReturn()
                .getResponse()
                .getContentAsString()
        );
        Route route = routeRepository.findByRouteId(1);

        assertThat(response.get("id")).isEqualTo(route.getId());
        assertThat(response.get("routeId")).isEqualTo(route.getRouteId());
        assertThat(response.get("originPlanetId")).isEqualTo(route.getOriginPlanetId());
        assertThat(response.get("targetPlanetId")).isEqualTo(route.getTargetPlanetId());
        assertThat(response.get("distance")).isEqualTo(route.getDistance());
    }

    @Test
    void testGetFakeRoute() throws Exception {
        mockMvc.perform(get("/route/404"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreateRoute() throws Exception {
        Route route = new Route(-1, 500, 1, 2, 100);

        String content = mapper.writeValueAsString(route);

        mockMvc.perform(post("/routes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated());
        route = routeRepository.findByRouteId(500);

        assertThat(route).isNotNull();
        assertThat(route.getId()).isNotEqualTo(-1);
        assertThat(route.getRouteId()).isEqualTo(500);
        assertThat(route.getOriginPlanetId()).isEqualTo(1);
        assertThat(route.getTargetPlanetId()).isEqualTo(2);
        assertThat(route.getDistance()).isEqualTo(100);
    }

    @Test
    void testUpdateRoute() throws Exception {
        Route route = new Route(-1, 600, 2, 3, 200);
        int oldRouteId = routeRepository.save(route).getId();

        route.setOriginPlanetId(3);
        route.setTargetPlanetId(2);

        String content = mapper.writeValueAsString(route);

        mockMvc.perform(put("/route/600")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().is2xxSuccessful());
        route = routeRepository.findByRouteId(600);

        assertThat(route).isNotNull();
        assertThat(route.getId()).isEqualTo(oldRouteId);
        assertThat(route.getOriginPlanetId()).isEqualTo(3);
        assertThat(route.getTargetPlanetId()).isEqualTo(2);
        assertThat(route.getDistance()).isEqualTo(200);

        mockMvc.perform(put("/route/404")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteRoute() throws Exception {
        Route route = new Route(-1, 700, 2, 3, 200);
        routeRepository.save(route);

        mockMvc.perform(delete("/route/700"))
                .andExpect(status().is2xxSuccessful());
        route = routeRepository.findByRouteId(700);

        assertThat(route).isNull();
    }

}
