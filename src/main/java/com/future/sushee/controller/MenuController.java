package com.future.sushee.controller;

import com.future.sushee.payload.request.MenuCreationRequest;
import com.future.sushee.payload.response.MessageResponse;
import com.future.sushee.service.interfaces.MenuService;
import com.future.sushee.model.Menu;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/menu")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MenuController {

    private final MenuService menuService;

    @GetMapping("")
    public List<Menu> getAllActiveMenu(){
        List<Menu> output = new ArrayList<>();
        List<Menu> menus = menuService.getAllMenu();
        for(Menu menu : menus){
            if(menu.getStatus()==1) output.add(menu);
        }
        return output;
    }

    @GetMapping("/all")
    public List<Menu> getAllMenu() {
        return menuService.getAllMenu();
    }

    @GetMapping("/{id}")
    public Menu getMenuById(@PathVariable Long id) {
        try {
            return menuService.getById(id);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No such menu with ID " + String.valueOf(id)
            );
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMenu(@Valid @RequestBody MenuCreationRequest menuCreationRequest) {
        Menu menu;
        try {
            menu = menuService.addMenuFromRequest(menuCreationRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(new MessageResponse(
                "Menu '"+ menu.getName() + "' successfully added (id: " + menu.getId() + ")."));
    }

    @PostMapping("/upsert/{id}")
    public ResponseEntity<?> upsertMenu(@Valid @RequestBody MenuCreationRequest menuCreationRequest,
                                        @PathVariable Long id) {
        Menu menu;
        try {
            menu = menuService.updateMenuFromRequest(id, menuCreationRequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
        return ResponseEntity.ok().body(new MessageResponse(
                "Menu '"+ menu.getName() + "' successfully updated (id: " + menu.getId() + ")."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable Long id) {
        menuService.delete(menuService.getById(id));
        return ResponseEntity.ok(new MessageResponse("Menu " + String.valueOf(id) + " successfully deleted"));
    }
}
