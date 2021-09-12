package com.future.sushee.service;

import com.future.sushee.model.Menu;
import com.future.sushee.model.Order;
import com.future.sushee.payload.request.MenuCreationRequest;
import com.future.sushee.repository.MenuRepository;
import com.future.sushee.service.implementations.MenuServiceImpl;
import com.future.sushee.service.interfaces.MenuService;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@Tag("service")
public class MenuServiceTest {
    @Spy
    @InjectMocks
    MenuServiceImpl menuService;

    @Mock
    MenuRepository menuRepository;

    @Captor
    ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    ArgumentCaptor<Menu> menuArgumentCaptor;

    private Menu menu1;
    private Menu menu2;
    private List<Menu> menus;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.menu1 = new Menu();
        this.menu2 = new Menu();
        this.menus = Arrays.asList(menu1, menu2);

    }

    @AfterEach()
    public void afterEach() {
        reset(menuRepository, menuService);
    }

    @Test
    public void getAllMenuTest() {
        when(menuRepository.findAll()).thenReturn(menus);
        List<Menu> result = menuService.getAllMenu();

        verify(menuRepository).findAll();
        assertEquals(result, menus);
    }

    @Test
    public void getByIdTest() {
        Long id = 1L;
        when(menuRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(menu1));
        Menu result = menuService.getById(id);

        verify(menuRepository).findById(longArgumentCaptor.capture());
        assertEquals(id, longArgumentCaptor.getValue());
        assertEquals(result, menu1);
    }

    @Test
    public void addMenuTest() {
        Menu result = menuService.addMenu(menu1);
        verify(menuRepository).save(menuArgumentCaptor.capture());

        assertEquals(result, menu1);
        assertEquals(menuArgumentCaptor.getValue(), menu1);
    }

    @Test
    public void addMenuFromRequestTest() {
        MenuCreationRequest menuCreationRequest =
                new MenuCreationRequest("menu name", "menu desc", "menu unit", "menu url", 1);

        doReturn(new Menu(1L, "menu name", "menu desc", "menu unit", "menu url", 1, null))
                .when(menuService).addMenu(menuArgumentCaptor.capture());

        try {
            Menu result = menuService.addMenuFromRequest(menuCreationRequest);
            verify(menuService).addMenu(menuArgumentCaptor.getValue());
            assertEquals(result.getName(), menuCreationRequest.getName());
            assertEquals(result.getDescription(), menuCreationRequest.getDescription());
            assertEquals(result.getImageUrl(), menuCreationRequest.getImageUrl());
            assertEquals(result.getUnit(), menuCreationRequest.getUnit());
            assertEquals(result.getStatus(), menuCreationRequest.getStatus());
        } catch (Exception e) {}
    }

    @Test
    public void updateMenuFromRequestTest() throws Exception {
        MenuCreationRequest menuCreationRequest =
            new MenuCreationRequest("menu name", "menu desc", "menu unit", "menu url", 1);

        doReturn(new Menu()).when(menuService).getById(1L);
        Menu result = menuService.updateMenuFromRequest(1L, menuCreationRequest);

        verify(menuService).getById(1L);
        assertEquals(result.getName(), menuCreationRequest.getName());
        assertEquals(result.getDescription(), menuCreationRequest.getDescription());
        assertEquals(result.getImageUrl(), menuCreationRequest.getImageUrl());
        assertEquals(result.getUnit(), menuCreationRequest.getUnit());
        assertEquals(result.getStatus(), menuCreationRequest.getStatus());
    }

    @Test
    public void deleteTest() {
        Menu result = menuService.delete(menu1);
        verify(menuRepository).delete(menu1);
        assertEquals(result, menu1);
    }
}
