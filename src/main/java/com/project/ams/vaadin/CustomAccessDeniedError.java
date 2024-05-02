package com.project.ams.vaadin;

import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.RouteAccessDeniedError;
import com.vaadin.flow.server.HttpStatusCode;

public class CustomAccessDeniedError extends RouteAccessDeniedError{
    @Override
    public int setErrorParameter(BeforeEnterEvent event,
            ErrorParameter<AccessDeniedException> parameter) {
        getElement().setText("Nothing to see here, please move on");
        return HttpStatusCode.UNAUTHORIZED.getCode();
    }
}