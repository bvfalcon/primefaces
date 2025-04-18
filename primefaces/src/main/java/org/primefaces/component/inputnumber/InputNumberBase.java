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
package org.primefaces.component.inputnumber;

import org.primefaces.component.api.AbstractPrimeHtmlInputText;
import org.primefaces.component.api.InputHolder;
import org.primefaces.component.api.Widget;
import org.primefaces.util.LocaleUtils;

public abstract class InputNumberBase extends AbstractPrimeHtmlInputText implements Widget, InputHolder {

    public static final String COMPONENT_FAMILY = "org.primefaces.component";

    public static final String DEFAULT_RENDERER = "org.primefaces.component.InputNumberRenderer";

    public enum PropertyKeys {
        caretPositionOnFocus,
        decimalPlaces,
        decimalPlacesRawValue,
        decimalSeparator,
        decimalSeparatorAlternative,
        emptyValue,
        inputStyle,
        inputStyleClass,
        leadingZero,
        maxValue,
        minValue,
        modifyValueOnUpDownArrow,
        modifyValueOnWheel,
        padControl,
        placeholder,
        roundMethod,
        selectOnFocus,
        signPosition,
        symbol,
        symbolPosition,
        thousandSeparator,
        widgetVar
    }

    public InputNumberBase() {
        setRendererType(DEFAULT_RENDERER);
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getPlaceholder() {
        return (String) getStateHelper().eval(PropertyKeys.placeholder, null);
    }

    public void setPlaceholder(String placeholder) {
        getStateHelper().put(PropertyKeys.placeholder, placeholder);
    }

    public String getWidgetVar() {
        return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
    }

    public void setWidgetVar(String widgetVar) {
        getStateHelper().put(PropertyKeys.widgetVar, widgetVar);
    }

    public String getSymbol() {
        return (String) getStateHelper().eval(PropertyKeys.symbol, null);
    }

    public void setSymbol(String symbol) {
        getStateHelper().put(PropertyKeys.symbol, symbol);
    }

    public String getSignPosition() {
        return (String) getStateHelper().eval(PropertyKeys.signPosition, null);
    }

    public void setSignPosition(String signPosition) {
        getStateHelper().put(PropertyKeys.signPosition, signPosition);
    }

    public String getSymbolPosition() {
        return (String) getStateHelper().eval(PropertyKeys.symbolPosition, null);
    }

    public void setSymbolPosition(String symbolPosition) {
        getStateHelper().put(PropertyKeys.symbolPosition, symbolPosition);
    }

    public String getMinValue() {
        return (String) getStateHelper().eval(PropertyKeys.minValue, null);
    }

    public void setMinValue(String minValue) {
        getStateHelper().put(PropertyKeys.minValue, minValue);
    }

    public String getMaxValue() {
        return (String) getStateHelper().eval(PropertyKeys.maxValue, null);
    }

    public void setMaxValue(String maxValue) {
        getStateHelper().put(PropertyKeys.maxValue, maxValue);
    }

    public String getRoundMethod() {
        return (String) getStateHelper().eval(PropertyKeys.roundMethod, "S");
    }

    public void setRoundMethod(String roundMethod) {
        getStateHelper().put(PropertyKeys.roundMethod, roundMethod);
    }

    public String getDecimalPlaces() {
        return (String) getStateHelper().eval(PropertyKeys.decimalPlaces, null);
    }

    public void setDecimalPlaces(String decimalPlaces) {
        getStateHelper().put(PropertyKeys.decimalPlaces, decimalPlaces);
    }

    public Integer getDecimalPlacesRawValue() {
        return (Integer) getStateHelper().eval(PropertyKeys.decimalPlacesRawValue, null);
    }

    public void setDecimalPlacesRawValue(Integer decimalPlacesRawValue) {
        getStateHelper().put(PropertyKeys.decimalPlacesRawValue, decimalPlacesRawValue);
    }

    public String getDecimalSeparator() {
        return (String) getStateHelper().eval(PropertyKeys.decimalSeparator,
            () -> LocaleUtils.getDecimalSeparator(getFacesContext()));
    }

    public void setDecimalSeparator(final String decimalSeparator) {
        getStateHelper().put(PropertyKeys.decimalSeparator, decimalSeparator);
    }

    public String getThousandSeparator() {
        return (String) getStateHelper().eval(PropertyKeys.thousandSeparator,
            () -> LocaleUtils.getThousandSeparator(getFacesContext()));
    }

    public void setThousandSeparator(final String thousandSeparator) {
        getStateHelper().put(PropertyKeys.thousandSeparator, thousandSeparator);
    }

    public String getEmptyValue() {
        return (String) getStateHelper().eval(PropertyKeys.emptyValue, "focus");
    }

    public void setEmptyValue(String emptyValue) {
        getStateHelper().put(PropertyKeys.emptyValue, emptyValue);
    }

    public String getInputStyle() {
        return (String) getStateHelper().eval(PropertyKeys.inputStyle, null);
    }

    public void setInputStyle(String inputStyle) {
        getStateHelper().put(PropertyKeys.inputStyle, inputStyle);
    }

    public String getInputStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.inputStyleClass, null);
    }

    public void setInputStyleClass(String inputStyleClass) {
        getStateHelper().put(PropertyKeys.inputStyleClass, inputStyleClass);
    }

    public String getPadControl() {
        return (String) getStateHelper().eval(PropertyKeys.padControl, "true");
    }

    public void setPadControl(String padControl) {
        getStateHelper().put(PropertyKeys.padControl, padControl);
    }

    public String getLeadingZero() {
        return (String) getStateHelper().eval(PropertyKeys.leadingZero, "allow");
    }

    public void setLeadingZero(String leadingZero) {
        getStateHelper().put(PropertyKeys.leadingZero, leadingZero);
    }

    public String getDecimalSeparatorAlternative() {
        return (String) getStateHelper().eval(PropertyKeys.decimalSeparatorAlternative, null);
    }

    public void setDecimalSeparatorAlternative(String decimalSeparatorAlternative) {
        getStateHelper().put(PropertyKeys.decimalSeparatorAlternative, decimalSeparatorAlternative);
    }

    public boolean isModifyValueOnWheel() {
        return (Boolean) getStateHelper().eval(PropertyKeys.modifyValueOnWheel, true);
    }

    public void setModifyValueOnWheel(boolean modifyValueOnWheel) {
        getStateHelper().put(PropertyKeys.modifyValueOnWheel, modifyValueOnWheel);
    }

    public boolean isModifyValueOnUpDownArrow() {
        return (Boolean) getStateHelper().eval(PropertyKeys.modifyValueOnUpDownArrow, true);
    }

    public void setModifyValueOnUpDownArrow(boolean modifyValueOnUpDownArrow) {
        getStateHelper().put(PropertyKeys.modifyValueOnUpDownArrow, modifyValueOnUpDownArrow);
    }

    public boolean isSelectOnFocus() {
        return (Boolean) getStateHelper().eval(PropertyKeys.selectOnFocus, true);
    }

    public void setSelectOnFocus(boolean selectOnFocus) {
        getStateHelper().put(PropertyKeys.selectOnFocus, selectOnFocus);
    }

    public String getCaretPositionOnFocus() {
        return (String) getStateHelper().eval(PropertyKeys.caretPositionOnFocus, null);
    }

    public void setCaretPositionOnFocus(String caretPositionOnFocus) {
        getStateHelper().put(PropertyKeys.caretPositionOnFocus, caretPositionOnFocus);
    }
}