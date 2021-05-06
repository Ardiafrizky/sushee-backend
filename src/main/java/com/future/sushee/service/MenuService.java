package com.future.sushee.service;

import com.future.sushee.model.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<Menu> getAllMenu();
    Menu add(Menu menu);
    Menu delete(Menu menu);
}
