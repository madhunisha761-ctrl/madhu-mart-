package com.madhunisha.madhumart.controller;

import com.google.gson.JsonObject;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/chatbot")
public class ChatbotServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String q = req.getParameter("message");
        JsonObject json = new JsonObject();
        json.addProperty("reply", answer(q));
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(json.toString());
    }

    static String answer(String message) {
        if (message == null || message.trim().isEmpty()) {
            return "Please type a question.";
        }
        String m = message.toLowerCase();
        if (m.contains("hello") || m.contains("hi")) {
            return "Hi! I am MadhuBot. Ask me about products, cart, orders, reviews or login.";
        }
        if (m.contains("register") || m.contains("sign up") || m.contains("account")) {
            return "Click Register on the home page, enter name, email, password and choose Buyer or Seller.";
        }
        if (m.contains("login") || m.contains("password")) {
            return "Use the Login link on the home page with your email and password.";
        }
        if (m.contains("search") || m.contains("find") || m.contains("category") || m.contains("product")) {
            return "Open Products, type a keyword in the search box or pick a category, then tap Search.";
        }
        if (m.contains("cart")) {
            return "Tap Add to Cart on any product. Open Cart to change quantity or remove items, then Checkout.";
        }
        if (m.contains("checkout") || m.contains("place order") || m.contains("buy")) {
            return "Open Cart, tap Checkout, enter your shipping address and tap Place Order.";
        }
        if (m.contains("order") || m.contains("history") || m.contains("track")) {
            return "Open Orders to see all your orders with status and items.";
        }
        if (m.contains("review") || m.contains("rating") || m.contains("star")) {
            return "You can review a product after your order is marked Completed. Open the product page and submit stars and text.";
        }
        if (m.contains("sell") || m.contains("seller") || m.contains("listing")) {
            return "Register as Seller, then open My Products to add, edit or delete your listings.";
        }
        return "Sorry, I did not understand. Try asking about products, cart, checkout, orders, reviews or login.";
    }
}
