package za.co.dinoko.assignment.davidPoulton.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import za.co.dinoko.assignment.davidPoulton.api.PlanetController;
import za.co.dinoko.assignment.davidPoulton.db.PlanetRepository;
import za.co.dinoko.assignment.davidPoulton.db.models.Planet;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PlanetTests {

    private ObjectMapper mapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlanetRepository planetRepository;

    @Test
    void testGetAllPlanets() throws Exception {
        // This test is very coarse, might be better to check more detailed information about each planet
        // but we can trust the per-planet test.
        JSONArray response = new JSONArray(mockMvc.perform(get("/planets"))
                .andReturn()
                .getResponse()
                .getContentAsString()
        );

        assertThat(response.length()).isEqualTo(planetRepository.findAll().size());
    }

    @Test
    void testGetSinglePlanet() throws Exception {
        JSONObject response = new JSONObject(mockMvc.perform(get("/planet/A"))
                .andReturn()
                .getResponse()
                .getContentAsString()
        );
        Planet planet = planetRepository.findByNode("A");

        assertThat(response.get("id")).isEqualTo(planet.getId());
        assertThat(response.get("node")).isEqualTo(planet.getNode());
        assertThat(response.get("name")).isEqualTo(planet.getName());
    }

    @Test
    void testGetFakePlanet() throws Exception {
        mockMvc.perform(get("/planet/FAKE"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreatePlanet() throws Exception {
        Planet planet = new Planet(-1, "CREATE", "Create");

        String content = mapper.writeValueAsString(planet);

        mockMvc.perform(post("/planets")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isCreated());
        planet = planetRepository.findByNode("CREATE");

        assertThat(planet).isNotNull();
        assertThat(planet.getNode()).isEqualTo("CREATE");
        assertThat(planet.getName()).isEqualTo("Create");
    }

    @Test
    void testUpdatePlanet() throws Exception {
        Planet planet = new Planet(0, "UPDATE", "Update");
        int oldPlanetId = planetRepository.save(planet).getId();

        planet.setName("Updated");
        planet.setNode(null);

        String content = mapper.writeValueAsString(planet);

        mockMvc.perform(put("/planet/UPDATE")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().is2xxSuccessful());
        planet = planetRepository.findByNode("UPDATE");

        assertThat(planet).isNotNull();
        assertThat(planet.getId()).isEqualTo(oldPlanetId);
        assertThat(planet.getNode()).isEqualTo("UPDATE");
        assertThat(planet.getName()).isEqualTo("Updated");

        mockMvc.perform(put("/planet/FAKE")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePlanet() throws Exception {
        Planet planet = new Planet(0, "DELETE", "Delete");
        planetRepository.save(planet);

        mockMvc.perform(delete("/planet/DELETE"))
                .andExpect(status().is2xxSuccessful());
        planet = planetRepository.findByNode("DELETE");

        assertThat(planet).isNull();
    }

}
