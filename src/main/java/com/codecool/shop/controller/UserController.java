package com.codecool.shop.controller;

import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.JDBC.UserDaoJDBC;
import com.codecool.shop.exception.ConnectToStorageFailed;
import com.codecool.shop.model.User;
import com.codecool.shop.utils.Mailer;
import com.codecool.shop.utils.PasswordStorage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import spark.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserController {

    public static String register(Request req, Response res) throws ConnectToStorageFailed, PasswordStorage.CannotPerformOperationException {



        String regData = req.body();
        //System.out.println(regData);
        Gson gson = new Gson();
        ArrayList<String> list = gson.fromJson(regData, new TypeToken<ArrayList<String>>() {}.getType());

        String userEmail = list.get(0);
        String passwordRaw = list.get(1);

        String passwordHashed = PasswordStorage.createHash(passwordRaw);


        User user = new User(userEmail);
        user.setPassword(passwordHashed);
        UserDaoJDBC userDaoJDBC = UserDaoJDBC.getInstance();

        if (userDaoJDBC.find(userEmail) == null){
            userDaoJDBC.add(user);
            Mailer.send("grannyshop.javengers@gmail.com","grannyshop", userEmail, "Registration", "Welcome to the GrannyWebshop");
        } else {
            return gson.toJson("failure");
        }

        return gson.toJson("success");
    }

    public static String login(Request request, Response response) throws ConnectToStorageFailed, PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        String loginData = request.body();
        System.out.println(loginData);
        Gson gson = new Gson();
        ArrayList<String> list = gson.fromJson(loginData, new TypeToken<ArrayList<String>>() {}.getType());

        String userEmail = list.get(0);
        String passwordRaw = list.get(1);

        UserDaoJDBC userDaoJDBC = UserDaoJDBC.getInstance();
        User user = userDaoJDBC.find(userEmail);

        String storedPassword = user.getPassword();
        request.session().attribute("user", user.getEmail());

        String userSession = request.session().attribute("user");

        if(PasswordStorage.verifyPassword(passwordRaw, storedPassword)){
            return gson.toJson("success");
        }
        return gson.toJson("failure");
    }

    public static Route logout(Request request, Response response) {
        request.session().removeAttribute("user");
        response.redirect("/");
        return null;
    }
}
