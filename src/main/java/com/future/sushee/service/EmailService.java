package com.future.sushee.service;

import com.future.sushee.model.Menu;

import java.util.List;

public interface EmailService {
    void sendEmail(String to, Long id, String username);
}
