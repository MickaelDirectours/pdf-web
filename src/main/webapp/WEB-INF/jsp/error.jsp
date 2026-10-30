<%--

/**
 * This JSP is used when exception occurs. It is mainly used when catching exception in controller.
 *
 * Required parameter :
 *   - error : an exception of type Exception containing message to display.
 */

--%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<html>

<head>

    <title>Erreur</title>
</head>

<body>
	IP: ${pageContext.request.localAddr}<br/>

            <c:if test="${not empty error}">

                <div class="mm-page mm-slideout wrapper">
                    <section class="contenu-static">
                        <div class="container text-center">

                            <hr />
                            <p>${error.message}</p>
                            <hr />

                            <div id="stack-trace">
                                <c:forEach var="stackTraceElement" items="${error.stackTrace}">
                                    <c:out value="${stackTraceElement}" />
                                    <br />
                                </c:forEach>
                            </div>

                        </div>
                    </section>
                </div>
            </c:if>
    </main>
</body>
</html>
