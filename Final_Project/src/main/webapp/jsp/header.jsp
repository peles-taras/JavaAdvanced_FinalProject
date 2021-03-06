<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>header</title>


    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Bitter:400,700">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC" crossorigin="anonymous">
        <link rel="stylesheet" href="/css/header.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
</head>
<body>
 <div>
        <div class="header-dark main-wrapper">
            <nav class="navbar navbar-dark navbar-expand-md navigation-clean-search">
				<div class="container">
					<a class="navbar-brand" href="${contextPath}/home"><spring:message code='header.title'/></a>
					<button class="navbar-toggler" data-toggle="collapse"
						data-target="#navcol-1">
						<span class="sr-only">Toggle navigation</span><span
							class="navbar-toggler-icon"></span>
					</button>
					<div class="collapse navbar-collapse" id="navcol-1">
						<ul class="nav navbar-nav header-items">
							<li class="nav-item" role="presentation"><a class="nav-link"
								href="${contextPath}/home"><spring:message code='header.home'/></a></li>
								<li class="nav-item" role="presentation">
								<security:authorize access="hasRole('ROLE_USER')">	
								<a class="nav-link" href="${contextPath}/profile/${pageContext.request.userPrincipal.name}"><spring:message code='header.profile'/></a>
							</security:authorize>
							</li>
                                     <security:authorize access="hasRole('ROLE_ADMINISTRATOR')">	
                                  <li class="nav-item" role="presentation"><a class="nav-link"
								href="${contextPath}/createFaculty"><spring:message code='header.createFaculty'/></a></li>
                              </security:authorize>


							<c:if test="${pageContext.request.userPrincipal.name != null}">
								<form id="logoutForm" method="POST"
									action="${contextPath}/logout"
									class="form-inline mr-auto login">
									<input type="hidden" name="${_csrf.parameterName}"
										value="${_csrf.token}" />
								</form>



							</c:if>

						</ul>
						
						<div class="name-logout">
						<a class="navbar-text welcome-text"><spring:message code='header.welcome'/> , ${pageContext.request.userPrincipal.name} |</a>
<a href="#" onclick="document.forms['logoutForm'].submit()" class="navbar-text logout-button"><spring:message code='header.logout'/></a>
						</div>

						<div class="localesWrapper">
							<fieldset>
								<label><spring:message code='header.langChoose'/></label>
								<select id="locales" >
									<option value="en" selected><spring:message code='header.english'/></option>
									<option value="ua"><spring:message code='header.ukrainian'/></option>

								</select>
							</fieldset>
						</div>


					</div>	
					
				</div>
			</nav>
            
            
        </div>
    </div>
<script src="/js/locales.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

</body>
</html>