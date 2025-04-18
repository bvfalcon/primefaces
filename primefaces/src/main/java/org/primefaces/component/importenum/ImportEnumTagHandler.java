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
package org.primefaces.component.importenum;

import org.primefaces.context.PrimeApplicationContext;
import org.primefaces.util.LangUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Logger;

import jakarta.faces.FacesException;
import jakarta.faces.application.ProjectStage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.facelets.FaceletContext;
import jakarta.faces.view.facelets.TagAttribute;
import jakarta.faces.view.facelets.TagConfig;
import jakarta.faces.view.facelets.TagHandler;

/**
 * {@link TagHandler} for the <code>ImportEnum</code> component.
 */
public class ImportEnumTagHandler extends TagHandler {
    private static final String DEFAULT_ALL_SUFFIX = "ALL_VALUES";
    private static final Logger LOG = Logger.getLogger(ImportEnumTagHandler.class.getName());

    private final TagAttribute typeTagAttribute;
    private final TagAttribute varTagAttribute;
    private final TagAttribute allSuffixTagAttribute;

    public ImportEnumTagHandler(TagConfig config) {
        super(config);

        typeTagAttribute = super.getRequiredAttribute("type");
        varTagAttribute = super.getAttribute("var");
        allSuffixTagAttribute = super.getAttribute("allSuffix");
    }

    @Override
    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {

        FacesContext facesContext = FacesContext.getCurrentInstance();

        Class<?> type = getClassFromAttribute(typeTagAttribute, ctx);
        Map<String, Object> enumValues = getEnumValues(facesContext, type,
                allSuffixTagAttribute == null ? null : allSuffixTagAttribute.getValue(ctx));

        // Create alias/var expression
        String var;
        if (varTagAttribute == null) {
            var = type.getSimpleName(); // fall back to class name
        }
        else {
            var = varTagAttribute.getValue(ctx);
        }

        ctx.setAttribute(var, enumValues);
    }

    /**
     * Gets the {@link Class} from the {@link TagAttribute}.
     *
     * @param attribute The {@link TagAttribute}.
     * @param ctx       The {@link FaceletContext}.
     * @return The {@link Class}.
     */
    protected Class<?> getClassFromAttribute(TagAttribute attribute, FaceletContext ctx) {
        String type = attribute.getValue(ctx);

        try {
            return LangUtils.loadClassForName(type);
        }
        catch (ClassNotFoundException e) {
            throw new FacesException("Class " + type + " not found.", e);
        }
    }

    /**
     * Get all enum values of the given {@link Class}.
     *
     * @param facesContext The {@link FacesContext}.
     * @param type         The enum class.
     * @param allSuffix    The suffix to access a array with all enum values.
     * @return A {@link Map} with the enum values.
     */
    protected Map<String, Object> getEnumValues(FacesContext facesContext, Class<?> type, String allSuffix) {

        if (type.isEnum()) {

            // allSuffix was not considered by the cache.
            // allSuffix now got deprecated. If still used, create a new map each time.
            boolean cacheEnabled = allSuffix == null && facesContext.isProjectStage(ProjectStage.Production);
            Map<Class<?>, Map<String, Object>> cache
                    = PrimeApplicationContext.getCurrentInstance(FacesContext.getCurrentInstance()).getEnumCacheMap();

            if (allSuffix != null && facesContext.isProjectStage(ProjectStage.Development)) {
                LOG.info("The allSuffix attribute of '<p:importEnum/>' is deprecated and will be removed in a "
                        + "future version. Please use the 'ALL_VALUES' attribute to access all enum values.");
            }

            Map<String, Object> enums;

            if (cacheEnabled && cache.containsKey(type)) {
                enums = cache.get(type);
            }
            else {
                enums = new EnumHashMap<>(type);

                enums.put(DEFAULT_ALL_SUFFIX, type.getEnumConstants());

                for (Object value : type.getEnumConstants()) {
                    Enum<?> currentEnum = (Enum<?>) value;
                    enums.put(currentEnum.name(), currentEnum);
                }

                if (allSuffix != null) {
                    enums.put(allSuffix, type.getEnumConstants());
                }

                enums = Collections.unmodifiableMap(enums);

                if (cacheEnabled) {
                    cache.put(type, enums);
                }
            }

            return enums;
        }
        else {
            throw new FacesException("Class '" + type + "' is not an enum.");
        }
    }
}
