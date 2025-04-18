/*
 * The MIT License
 *
 * Copyright (c) 2009-2025 PrimeTek Informatics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.primefaces.application.exceptionhandler;

import org.primefaces.util.LangUtils;

import java.util.Map;

import jakarta.el.ELContext;
import jakarta.el.ELResolver;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.lifecycle.ClientWindow;

public class PrimeExceptionHandlerELResolver extends ELResolver {

    public static final String EL_NAME = "pfExceptionHandler";

    @Override
    public Object getValue(ELContext elContext, Object base, Object property) {

        if (EL_NAME.equals(property)) {
            elContext.setPropertyResolved(true);

            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();

            ExceptionInfo info = (ExceptionInfo) context.getAttributes().get(ExceptionInfo.ATTRIBUTE_NAME);

            if (info == null && externalContext.getSession(false) != null) {
                ClientWindow clientWindow = externalContext.getClientWindow();
                if (clientWindow != null && LangUtils.isNotBlank(clientWindow.getId())) {
                    Map<String, ExceptionInfo> windowsMap = (Map<String, ExceptionInfo>)
                            externalContext.getSessionMap().get(ExceptionInfo.ATTRIBUTE_NAME + "_map");
                    if (windowsMap != null) {
                        info = windowsMap.get(clientWindow.getId());
                    }
                }
                if (info == null) {
                    info = (ExceptionInfo) externalContext.getSessionMap().get(ExceptionInfo.ATTRIBUTE_NAME);
                }
            }

            return info;
        }

        return null;
    }

    @Override
    public Class<?> getType(ELContext context, Object base, Object property) {
        return null;
    }

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) {
        // readonly
    }

    @Override
    public boolean isReadOnly(ELContext context, Object base, Object property) {
        return true;
    }

    @Override
    public Class<?> getCommonPropertyType(ELContext context, Object base) {
        return null;
    }
}
