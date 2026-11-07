package com.futrack.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest @AutoConfigureMockMvc
class JobApplicationControllerTest {
    @Autowired MockMvc mvc;
    private String create(String company,String title,String status) throws Exception {
        String body="""
            {"company":"%s","title":"%s","location":"Remote","type":"Full-time","mode":"Remote","date":"2026-11-05","status":"%s"}
            """.formatted(company,title,status);
        return mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
    }
    @Test void createsSearchesUpdatesAndDeletesApplications() throws Exception {
        String created=create("QA Company","Backend Engineer","Applied");
        String id=created.replaceAll(".*\"id\":\"([^\"]+)\".*","$1");
        mvc.perform(get("/api/applications?q=qa")).andExpect(status().isOk()).andExpect(jsonPath("$[0].company").value("QA Company"));
        mvc.perform(patch("/api/applications/"+id+"/status").contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"Interview\"}"))
            .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("Interview"));
        mvc.perform(delete("/api/applications/"+id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/applications/"+id)).andExpect(status().isNotFound());
    }
    @Test void rejectsMissingRequiredFields() throws Exception {
        mvc.perform(post("/api/applications").contentType(MediaType.APPLICATION_JSON).content("{\"company\":\"\",\"title\":\"\"}"))
            .andExpect(status().isBadRequest());
    }
}
