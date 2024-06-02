package com.example.desafiopicpay.services;

import com.example.desafiopicpay.domain.user.User;
import com.example.desafiopicpay.domain.user.UserType;
import com.example.desafiopicpay.dtos.UserDto;
import com.example.desafiopicpay.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if(sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuario do tipo Lojista nao esta autorizado a realizar transacao");
        }

        if(sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo insuficiente");
        }
    }

    public User findUserById(Long id) throws Exception {
        return this.repository.findUserById(id).orElseThrow(() -> new Exception("Usuario nao encontrado"));
    }

    public User createUser(UserDto userDto) {
        User user = new User(userDto);
        this.saveUser(user);

        return user;
    }

    public void saveUser(User user) {
        this.repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }
}
