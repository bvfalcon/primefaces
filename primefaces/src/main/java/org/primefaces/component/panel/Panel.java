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
package org.primefaces.component.panel;

import org.primefaces.PrimeFaces;
import org.primefaces.component.menu.Menu;
import org.primefaces.el.ValueExpressionAnalyzer;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.Visibility;
import org.primefaces.util.ComponentUtils;
import org.primefaces.util.Constants;
import org.primefaces.util.FacetUtils;
import org.primefaces.util.MapBuilder;

import java.util.Collection;
import java.util.Map;

import jakarta.el.ELContext;
import jakarta.el.ValueExpression;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.event.BehaviorEvent;
import jakarta.faces.event.FacesEvent;

@ResourceDependency(library = "primefaces", name = "components.css")
@ResourceDependency(library = "primefaces", name = "jquery/jquery.js")
@ResourceDependency(library = "primefaces", name = "jquery/jquery-plugins.js")
@ResourceDependency(library = "primefaces", name = "core.js")
@ResourceDependency(library = "primefaces", name = "components.js")
public class Panel extends PanelBase {

    public static final String COMPONENT_TYPE = "org.primefaces.component.Panel";

    public static final String PANEL_CLASS = "ui-panel ui-widget ui-widget-content";
    public static final String PANEL_TITLEBAR_CLASS = "ui-panel-titlebar ui-widget-header ui-helper-clearfix";
    public static final String PANEL_TITLE_CLASS = "ui-panel-title";
    public static final String PANEL_TITLE_ICON_CLASS = "ui-panel-titlebar-icon ui-state-default";
    public static final String PANEL_CONTENT_CLASS = "ui-panel-content ui-widget-content";
    public static final String PANEL_FOOTER_CLASS = "ui-panel-footer ui-widget-content";
    public static final String PANEL_ACTIONS_CLASS = "ui-panel-actions";

    private static final Map<String, Class<? extends BehaviorEvent>> BEHAVIOR_EVENT_MAPPING = MapBuilder.<String, Class<? extends BehaviorEvent>>builder()
            .put("toggle", ToggleEvent.class)
            .put("close", CloseEvent.class)
            .build();

    private static final Collection<String> EVENT_NAMES = BEHAVIOR_EVENT_MAPPING.keySet();

    @Override
    public Map<String, Class<? extends BehaviorEvent>> getBehaviorEventMapping() {
        return BEHAVIOR_EVENT_MAPPING;
    }

    @Override
    public Collection<String> getEventNames() {
        return EVENT_NAMES;
    }

    public Menu getOptionsMenu() {
        UIComponent optionsFacet = getFacet("options");
        if (FacetUtils.shouldRenderFacet(optionsFacet)) {
            if (optionsFacet instanceof Menu) {
                return (Menu) optionsFacet;
            }
            else {
                return (Menu) optionsFacet.getChildren().get(0);
            }
        }
        return null;
    }

    @Override
    public void queueEvent(FacesEvent event) {
        FacesContext context = getFacesContext();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String eventName = params.get(Constants.RequestParams.PARTIAL_BEHAVIOR_EVENT_PARAM);
        String clientId = getClientId(context);

        if (ComponentUtils.isRequestSource(this, context)) {
            AjaxBehaviorEvent behaviorEvent = (AjaxBehaviorEvent) event;

            if ("toggle".equals(eventName)) {
                boolean collapsed = Boolean.parseBoolean(params.get(clientId + "_collapsed"));
                Visibility visibility = collapsed ? Visibility.HIDDEN : Visibility.VISIBLE;

                ToggleEvent eventToQueue = new ToggleEvent(this, behaviorEvent.getBehavior(), visibility);
                eventToQueue.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(new ToggleEvent(this, behaviorEvent.getBehavior(), visibility));

            }
            else if ("close".equals(eventName)) {
                CloseEvent eventToQueue = new CloseEvent(this, behaviorEvent.getBehavior());
                eventToQueue.setPhaseId(behaviorEvent.getPhaseId());
                super.queueEvent(eventToQueue);
            }
        }
        else {
            super.queueEvent(event);
        }
    }

    @Override
    public void processDecodes(FacesContext context) {
        if (ComponentUtils.isRequestSource(this, context)) {
            decode(context);
        }
        else {
            super.processDecodes(context);
        }
    }

    @Override
    public void processValidators(FacesContext context) {
        if (!ComponentUtils.isRequestSource(this, context)) {
            super.processValidators(context);
        }
    }

    @Override
    public void processUpdates(FacesContext context) {
        if (!ComponentUtils.isRequestSource(this, context)) {
            super.processUpdates(context);
        }

        ELContext elContext = context.getELContext();
        ValueExpression expr = ValueExpressionAnalyzer.getExpression(elContext,
                getValueExpression(PropertyKeys.collapsed.toString()), true);
        if (expr != null && !expr.isReadOnly(elContext)) {
            expr.setValue(elContext, isCollapsed());
            getStateHelper().remove(PropertyKeys.collapsed);
        }
    }

    @Override
    public void restoreMultiViewState() {
        PanelState ps = getMultiViewState(false);
        if (ps != null) {
            setCollapsed(ps.isCollapsed());
        }
    }

    @Override
    public PanelState getMultiViewState(boolean create) {
        FacesContext fc = getFacesContext();
        String viewId = fc.getViewRoot().getViewId();

        return PrimeFaces.current().multiViewState()
                .get(viewId, getClientId(fc), create, PanelState::new);
    }

    @Override
    public void resetMultiViewState() {
        setCollapsed(false);
    }
}