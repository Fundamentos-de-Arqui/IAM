package com.soulware.pe.iam.interfaces.REST;

import com.soulware.pe.iam.interfaces.REST.dto.ApiResponse;
import com.soulware.pe.iam.interfaces.REST.dto.UserInfoResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/protected")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProtectedController {
    
    @GET
    @Path("/profile")
    public Response getUserProfile(@Context HttpServletRequest request) {
        try {
            String identityDocumentNumber = (String) request.getAttribute("identityDocumentNumber");
            Long userId = (Long) request.getAttribute("userId");
            
            if (identityDocumentNumber == null || userId == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ApiResponse(false, "Authentication required"))
                        .build();
            }
            
            return Response.ok(new UserInfoResponse(userId, identityDocumentNumber))
                    .build();
                    
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(false, "Failed to get user profile: " + e.getMessage()))
                    .build();
        }
    }
    
    @GET
    @Path("/test")
    public Response testProtectedEndpoint(@Context HttpServletRequest request) {
        String identityDocumentNumber = (String) request.getAttribute("identityDocumentNumber");
        return Response.ok(new ApiResponse(true, "Hello " + identityDocumentNumber + "! This is a protected endpoint."))
                .build();
    }
}
