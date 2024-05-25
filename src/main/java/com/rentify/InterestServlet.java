package com.rentify;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class InterestServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"unauthorized\"}");
            return;
        }

        int propertyId = Integer.parseInt(request.getParameter("propertyId"));
        Property property = PropertyService.getPropertyById(propertyId);

        if (property != null) {
            User seller = UserService.getUserByEmail(property.getSellerEmail());
            String sellerDetails = String.format("Name: %s %s, Email: %s, Phone: %s",
                    seller.getFirstName(), seller.getLastName(), seller.getEmail(), seller.getPhone());

            // Send email to buyer and seller
            User buyer = (User) session.getAttribute("user");
            EmailService.sendEmail(buyer.getEmail(), "Property Interest", sellerDetails);
            EmailService.sendEmail(seller.getEmail(), "New Interest", String.format("Buyer: %s %s, Email: %s, Phone: %s",
                    buyer.getFirstName(), buyer.getLastName(), buyer.getEmail(), buyer.getPhone()));

            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"success\", \"sellerDetails\":\"" + sellerDetails + "\"}");
        }
    }
}

