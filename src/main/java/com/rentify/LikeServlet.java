package com.rentify;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class LikeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int propertyId = Integer.parseInt(request.getParameter("propertyId"));
        PropertyService.likeProperty(propertyId);

        Property property = PropertyService.getPropertyById(propertyId);
        if (property != null) {
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"success\", \"likeCount\":" + property.getLikeCount() + "}");
        } else {
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"error\"}");
        }
    }
}

