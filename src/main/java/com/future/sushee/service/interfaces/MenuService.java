package com.future.sushee.service.interfaces;

import com.future.sushee.model.Menu;
import com.future.sushee.payload.request.MenuCreationRequest;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<Menu> getAllMenu();
    Menu addMenu(Menu menu);
    Menu addMenuFromRequest(MenuCreationRequest menuRequest);
    Menu getById(Long id);
    Menu delete(Menu menu);
}
