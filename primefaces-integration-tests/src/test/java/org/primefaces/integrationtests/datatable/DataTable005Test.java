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
package org.primefaces.integrationtests.datatable;

import org.primefaces.selenium.PrimeSelenium;
import org.primefaces.selenium.component.CommandButton;
import org.primefaces.selenium.component.DataTable;
import org.primefaces.selenium.component.Messages;

import java.util.stream.Stream;

import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataTable005Test extends AbstractDataTableTest {

    @ParameterizedTest
    @MethodSource("provideXhtmls")
    @Order(1)
    @DisplayName("DataTable: selection - multiple")
    void selectionMultiple(String xhtml) {
        // Arrange
        goTo(xhtml);
        DataTable dataTable = getDataTable();
        assertNotNull(dataTable);

        // Act
        dataTable.getCell(0, 0).getWebElement().click();
        Actions actions = new Actions(getWebDriver());
        actions.keyDown(Keys.META).click(dataTable.getCell(2, 0).getWebElement()).keyUp(Keys.META).perform();
        actions.keyDown(Keys.SHIFT).click(dataTable.getCell(4, 0).getWebElement()).keyUp(Keys.SHIFT).perform();
        getButton().click();

        // Assert
        dataTable = getDataTable();
        assertConfiguration(dataTable.getWidgetConfiguration());
        assertMessage("Selected ProgrammingLanguage(s)", "1,3,4,5");

        // Act
        getButtonUpdate().click();
        getButton().click();

        // Assert - selection must not be lost after update
        dataTable = getDataTable();
        assertConfiguration(dataTable.getWidgetConfiguration());
        assertMessage("Selected ProgrammingLanguage(s)", "1,3,4,5");
    }

    @ParameterizedTest
    @MethodSource("provideXhtmls")
    @Order(2)
    @DisplayName("DataTable: GitHub #7368 Selection with filtering")
    void selectionWithFilter(String xhtml) {
        // Arrange
        goTo(xhtml);
        DataTable dataTable = getDataTable();
        assertNotNull(dataTable);

        // Act
        dataTable.getCell(0, 0).getWebElement().click();
        Actions actions = new Actions(getWebDriver());
        actions.keyDown(Keys.META).click(dataTable.getCell(2, 0).getWebElement()).keyUp(Keys.META).perform();
        actions.keyDown(Keys.SHIFT).click(dataTable.getCell(4, 0).getWebElement()).keyUp(Keys.SHIFT).perform();
        getButton().click();

        // Assert
        dataTable = getDataTable();
        assertConfiguration(dataTable.getWidgetConfiguration());
        assertMessage("Selected ProgrammingLanguage(s)", "1,3,4,5");

        // Act - filter
        dataTable.filter("Name", "Java");
        getButtonUpdate().click();
        getButton().click();

        // Assert - selection must not be lost after filter and update
        dataTable = getDataTable();
        assertConfiguration(dataTable.getWidgetConfiguration());
        assertMessage("Selected ProgrammingLanguage(s)", "1,3,4,5");

        // Act - clear filter
        dataTable.removeFilter("Name");
        getButtonUpdate().click();
        getButton().click();

        // Assert - selection must not be lost after filter and update
        dataTable = getDataTable();
        assertConfiguration(dataTable.getWidgetConfiguration());
        assertMessage("Selected ProgrammingLanguage(s)", "1,3,4,5");
    }

    @ParameterizedTest
    @MethodSource("provideXhtmls")
    @Order(3)
    @DisplayName("DataTable:GitHub #3551 selection remain selected after AJAX update of table")
    void selectionUpdateKeepSelection(String xhtml) {
        // Arrange
        goTo(xhtml);
        DataTable dataTable = getDataTable();
        assertNotNull(dataTable);

        // Act
        Actions actions = new Actions(getWebDriver());
        actions.keyDown(Keys.META).click(dataTable.getCell(2, 0).getWebElement()).keyUp(Keys.META).perform();
        actions.keyDown(Keys.SHIFT).click(dataTable.getCell(4, 0).getWebElement()).keyUp(Keys.SHIFT).perform();
        getButtonProcess().click();

        // Assert
        dataTable = getDataTable();
        assertConfiguration(dataTable.getWidgetConfiguration());

        // make sure rows 3,4,5 are still selected and 0,1 not selected
        assertCss(dataTable.getRow(0).getWebElement(), "ui-widget-content", "ui-datatable-even", "ui-datatable-selectable");
        assertCss(dataTable.getRow(1).getWebElement(), "ui-widget-content", "ui-datatable-odd", "ui-datatable-selectable");
        assertCss(dataTable.getRow(2).getWebElement(), "ui-widget-content", "ui-datatable-even", "ui-datatable-selectable", "ui-state-highlight");
        assertCss(dataTable.getRow(3).getWebElement(), "ui-widget-content", "ui-datatable-odd", "ui-datatable-selectable", "ui-state-highlight");
        assertCss(dataTable.getRow(4).getWebElement(), "ui-widget-content", "ui-datatable-even", "ui-datatable-selectable", "ui-state-highlight");
    }

    private void assertMessage(String summary, String detail) {
        assertTrue(getMessages().getMessage(0).getSummary().contains(summary));
        assertTrue(getMessages().getMessage(0).getDetail().contains(detail));
    }

    private void assertConfiguration(JSONObject cfg) {
        assertNoJavascriptErrors();
        System.out.println("DataTable Config = " + cfg);
        assertTrue(cfg.has("selectionMode"));
    }

    private static Stream<Arguments> provideXhtmls() {
        return Stream.of(
                Arguments.of("datatable/dataTable005.xhtml"),
                Arguments.of("datatable/dataTable005Array.xhtml"));
    }

    private Messages getMessages() {
        return PrimeSelenium.createFragment(Messages.class, By.id("form:msgs"));
    }

    private DataTable getDataTable() {
        return PrimeSelenium.createFragment(DataTable.class, By.id("form:datatable"));
    }

    private CommandButton getButton() {
        return PrimeSelenium.createFragment(CommandButton.class, By.id("form:button"));
    }

    private CommandButton getButtonUpdate() {
        return PrimeSelenium.createFragment(CommandButton.class, By.id("form:buttonUpdate"));
    }

    private CommandButton getButtonProcess() {
        return PrimeSelenium.createFragment(CommandButton.class, By.id("form:buttonProcess"));
    }
}
