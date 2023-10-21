<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:if test="${sessionScope.isLoggedIn}">
    <c:redirect url="../index.jsp"/>
</c:if>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Login Page</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-6 col-lg-5">
            <h3 class="text-center text-secondary mt-5 mb-3">User Login</h3>
            <form method="post" action="Login" class="border rounded w-100 mb-5 mx-auto px-3 pt-3 bg-light">
                <div class="form-group">
                    <label for="username">Username</label>
                    <input name="username" id="username" type="text" class="form-control" placeholder="Username">
                </div>
                <div class="form-group">
                    <label for="password">Password</label>
                    <input name="password" id="password" type="password" class="form-control" placeholder="Password">
                </div>
                <div class="form-group">
                    <input name="remember" id="remember" type="checkbox">
                    <label for="remember">Remember me</label>
                </div>
                <c:choose>
                    <c:when test="${not empty flashMessage}">
                        <div class="alert alert-danger">
                            ${flashMessage}
                        </div>
                    </c:when>
                </c:choose>
                <div class="form-group">
                    <button type="submit" class="btn btn-success px-5">Login</button>
                </div>
            </form>

        </div>
    </div>
</div>

</body>

<%
    Cookie[] cookies = request.getCookies();
    Boolean loggedIn = false;
    if (session.getAttribute("userId") != null) {
        loggedIn = true;
    }
    if (!loggedIn && cookies != null) {
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("userId")) {
                loggedIn = true;
                break;
            }
        }
    }

    if (loggedIn) {
        response.sendRedirect("/Lab05/Product");
    }
%>
</html>
