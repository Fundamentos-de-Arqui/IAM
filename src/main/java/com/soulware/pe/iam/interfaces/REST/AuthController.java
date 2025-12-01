package com.soulware.pe.iam.interfaces.REST;

import com.soulware.pe.iam.application.LoginUserService;
import com.soulware.pe.iam.application.RegisterUserService;
import com.soulware.pe.iam.domain.UserRepository;
import com.soulware.pe.iam.infrastructure.persistence.ConnectionFactory;
import com.soulware.pe.iam.infrastructure.persistence.PostgresUserRepository;
import com.soulware.pe.iam.interfaces.REST.dto.LoginRequest;
import com.soulware.pe.iam.interfaces.REST.dto.LoginResponseDto;
import com.soulware.pe.iam.interfaces.REST.dto.RegisterRequest;
import com.soulware.pe.iam.interfaces.REST.dto.ApiResponse;
import com.soulware.pe.iam.interfaces.REST.dto.RegisterRequestExtended;
import com.soulware.pe.iam.domain.User;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/api/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {
    
    private final LoginUserService loginUserService;
    private final RegisterUserService registerUserService;
    
    public AuthController() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        UserRepository userRepository = new PostgresUserRepository(connectionFactory);
        this.loginUserService = new LoginUserService(userRepository);
        this.registerUserService = new RegisterUserService(userRepository);
        
        // Initialize database on startup
        try {
            connectionFactory.initializeDatabase();
        } catch (Exception e) {
            System.err.println("Failed to initialize database: " + e.getMessage());
        }
    }
    
    // REGISTRO VIA REST DESHABILITADO - Solo se permite registro via ActiveMQ
    /*
    @POST
    @Path("/register")
    public Response register(RegisterRequest request) {
        try {
            if (request == null || request.getUsername() == null || request.getPassword() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ApiResponse(false, "Username and password are required"))
                        .build();
            }
            
            // For basic registration, use username as DNI and default values
            registerUserService.register(
                request.getUsername(), // Using username as DNI for backward compatibility
                request.getPassword(),
                User.AccountType.PATIENT, // Default account type
                "DNI" // Default document type
            );
            
            return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse(true, "User registered successfully"))
                    .build();
                    
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage()))
                    .build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ApiResponse(false, e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(false, "Registration failed: " + e.getMessage()))
                    .build();
        }
    }
    */
    
    // REGISTRO EXTENDIDO VIA REST DESHABILITADO - Solo se permite registro via ActiveMQ
    /*
    @POST
    @Path("/register-extended")
    public Response registerExtended(RegisterRequestExtended request) {
        try {
            if (request == null || request.getPassword() == null || request.getAccountType() == null ||
                request.getDocumentType() == null || request.getIdentityDocumentNumber() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ApiResponse(false, "Password, account type, document type, and identity document number are required"))
                        .build();
            }
            
            User.AccountType accountType = User.AccountType.valueOf(request.getAccountType());
            
            User registeredUser = registerUserService.registerWithDetails(
                request.getPassword(),
                accountType,
                request.getDocumentType(),
                request.getIdentityDocumentNumber()
            );
            
            return Response.status(Response.Status.CREATED)
                    .entity(new ApiResponse(true, "User registered successfully with ID: " + registeredUser.getId()))
                    .build();
                    
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ApiResponse(false, e.getMessage()))
                    .build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(new ApiResponse(false, e.getMessage()))
                    .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(false, "Registration failed: " + e.getMessage()))
                    .build();
        }
    }
    */
    
    @POST
    @Path("/login")
    public Response login(LoginRequest request) {
        try {
            if (request == null || request.getIdentityDocumentNumber() == null || request.getPassword() == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ApiResponse(false, "Identity document number and password are required"))
                        .build();
            }
            
            LoginUserService.LoginResult result = loginUserService.login(
                    request.getIdentityDocumentNumber(), 
                    request.getPassword()
            );
            
            if (result.isSuccess()) {
                return Response.ok(new LoginResponseDto(
                        result.getUserId(), 
                        result.getAccountType(), 
                        result.getToken(), 
                        result.getMessage(),
                        result.getProfileId()))
                        .build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(new ApiResponse(false, result.getMessage()))
                        .build();
            }
            
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ApiResponse(false, "Login failed: " + e.getMessage()))
                    .build();
        }
    }
    
    @GET
    @Path("/health")
    public Response health() {
        return Response.ok(new ApiResponse(true, "IAM Service is running"))
                .build();
    }
}
