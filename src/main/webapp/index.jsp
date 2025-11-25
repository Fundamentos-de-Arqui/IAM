<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>IAM REST API</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 40px; background-color: #f5f5f5; }
        .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
        h1 { color: #333; text-align: center; }
        .endpoint { background: #f8f9fa; padding: 15px; margin: 10px 0; border-radius: 5px; border-left: 4px solid #007bff; }
        .method { font-weight: bold; color: #007bff; }
        .url { font-family: monospace; background: #e9ecef; padding: 2px 6px; border-radius: 3px; }
        .description { margin-top: 5px; color: #666; }
        .example { background: #f1f3f4; padding: 10px; margin: 10px 0; border-radius: 3px; font-family: monospace; font-size: 12px; }
    </style>
</head>
<body>
<div class="container">
    <h1>🔐 IAM REST API</h1>
    <p>Identity and Access Management REST API with JWT Authentication</p>
    
    <h2>Available Endpoints:</h2>
    
    <div class="endpoint">
        <div><span class="method">GET</span> <span class="url">/api/auth/health</span></div>
        <div class="description">Check if the service is running</div>
    </div>
    
    <!-- REGISTRO VIA REST DESHABILITADO - Solo disponible via ActiveMQ -->
    <div class="endpoint" style="background: #f8d7da; border-left: 4px solid #dc3545;">
        <div><span class="method" style="color: #dc3545;">DISABLED</span> <span class="url">/api/auth/register</span></div>
        <div class="description">Registration via REST is disabled. Use ActiveMQ queue 'iam_register' instead.</div>
    </div>
    
    <div class="endpoint">
        <div><span class="method">POST</span> <span class="url">/api/auth/login</span></div>
        <div class="description">Login with DNI and get JWT token</div>
        <div class="example">
{
  "identityDocumentNumber": "12345678",
  "password": "password123"
}
        </div>
    </div>
    
    <div class="endpoint">
        <div><span class="method">GET</span> <span class="url">/api/protected/profile</span></div>
        <div class="description">Get user profile (requires JWT token in Authorization header)</div>
        <div class="example">Authorization: Bearer &lt;your-jwt-token&gt;</div>
    </div>
    
    <div class="endpoint">
        <div><span class="method">GET</span> <span class="url">/api/protected/test</span></div>
        <div class="description">Test protected endpoint (requires JWT token)</div>
        <div class="example">Authorization: Bearer &lt;your-jwt-token&gt;</div>
    </div>
    
    <h2>Database Connection:</h2>
    <p>Connected to PostgreSQL database on Neon.tech</p>
    
    <h2>Features:</h2>
    <ul>
        <li>🔐 <strong>User Login via REST</strong> - JWT token generation with DNI authentication</li>
        <li>📝 <strong>User Registration via ActiveMQ</strong> - Asynchronous registration through message queues</li>
        <li>✅ JWT Authentication for protected endpoints</li>
        <li>✅ Multiple account types (PATIENT, LEGAL_RESPONSIBLE, ADMIN, THERAPIST)</li>
        <li>✅ Password hashing with BCrypt</li>
        <li>✅ Document-based user identification (DNI)</li>
        <li>✅ CORS support for web applications</li>
        <li>✅ PostgreSQL database integration</li>
        <li>✅ WAR deployment ready</li>
    </ul>
    
    <h2>ActiveMQ Integration:</h2>
    <ul>
        <li>📥 <strong>Input Queue:</strong> iam_register</li>
        <li>📤 <strong>Output Queue:</strong> apigateway_register</li>
        <li>🔄 Automatic message processing for user registration</li>
        <li>⚡ Asynchronous communication with API Gateway</li>
    </ul>
</div>
</body>
</html>