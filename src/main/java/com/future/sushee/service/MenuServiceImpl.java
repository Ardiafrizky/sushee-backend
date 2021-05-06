package com.future.sushee.service;

import com.future.sushee.model.Menu;
import com.future.sushee.repository.MenuRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public List<Menu> getAllMenu() { return menuRepository.findAll(); }

    @Override
    public Menu add(Menu menu) {
        menuRepository.save(menu);
        return menu;
    }

    @Override
    public Menu delete(Menu menu) {
        menuRepository.delete(menu);
        return menu;
    }
}
