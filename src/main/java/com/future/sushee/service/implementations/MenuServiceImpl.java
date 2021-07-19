package com.future.sushee.service.implementations;

import com.future.sushee.model.Menu;
import com.future.sushee.payload.request.MenuCreationRequest;
import com.future.sushee.repository.MenuRepository;
import com.future.sushee.service.interfaces.MenuService;
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
    public Menu getById(Long id) { return menuRepository.findById(id).get(); }

    @Override
    public Menu addMenu(Menu menu) {
        menuRepository.save(menu);
        return menu;
    }

    @Override
    public Menu addMenuFromRequest(MenuCreationRequest menuCreationRequest) {
        Menu menu = new Menu();
        menu.setName(menuCreationRequest.getName());
        menu.setDescription(menuCreationRequest.getDescription());
        menu.setUnit(menuCreationRequest.getUnit());
        menu.setImageUrl(menuCreationRequest.getImageUrl());
        menu.setStatus(menuCreationRequest.getStatus());
        return addMenu(menu);
    }

    @Override
    public Menu delete(Menu menu) {
        menuRepository.delete(menu);
        return menu;
    }
}
