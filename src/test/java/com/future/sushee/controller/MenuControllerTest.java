package com.future.sushee.controller;

import com.future.sushee.model.Menu;
import com.future.sushee.payload.request.MenuCreationRequest;
import com.future.sushee.service.implementations.MenuServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Tag("controller")
@SpringBootTest
@AutoConfigureMockMvc
public class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuServiceImpl menuService;

    private Menu menu1;
    private Menu menu2;
    private List<Menu> menus;
    private MenuCreationRequest menuCreationRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.menu1 = new Menu(1L, "menu1", "desc1", "pc", "url1", 1, null);
        this.menu2 = new Menu(2L, "menu2", "desc2", "pc", "url2", 0, null);
        this.menus = Arrays.asList(menu1, menu2);
        this.menuCreationRequest = new MenuCreationRequest("menu3", "desc3", "pc", "url3", 1);
    }

    @Test
    public void getAllActiveMenuTest() throws Exception {
        when(menuService.getAllMenu()).thenReturn(menus);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/menu")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("menu1")));
    }

    @Test
    public void getAllMenuTest() throws Exception {
        when(menuService.getAllMenu()).thenReturn(menus);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/menu/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("menu1")))
                .andExpect(jsonPath("$[1].name", is("menu2")));
    }

    @Test
    public void getMenuByIdSuccessTest() throws Exception {
        when(menuService.getById(ArgumentMatchers.anyLong())).thenReturn(menu1);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/menu/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("menu1")));
    }

    @Test
    public void getMenuByIdNotFoundTest() throws Exception {
        when(menuService.getById(ArgumentMatchers.anyLong())).thenThrow(NoSuchElementException.class);

        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/menu/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addMenuTest() throws Exception {
        when(menuService.addMenuFromRequest(ArgumentMatchers.any())).thenReturn(menu1);

        this.mockMvc.perform(MockMvcRequestBuilders.post("/api/menu/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"name\": \"menu3\",\n" +
                        "    \"description\": \"desc3\",\n" +
                        "    \"unit\": \"pc\",\n" +
                        "    \"imageUrl\": \"url3\",\n" +
                        "    \"status\": 1\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Menu 'menu1' successfully added (id: 1).")));
    }

    @Test
    public void deleteMenuTest() throws Exception {
        when(menuService.delete(ArgumentMatchers.any())).thenReturn(null);

        this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/menu/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Menu 1 successfully deleted")));
    }
}
