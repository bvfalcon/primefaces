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
package org.primefaces.component.api;

import org.primefaces.model.ColumnMeta;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;

import java.io.Serializable;
import java.util.Map;

public class UITableState implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, SortMeta> sortBy;

    private Map<String, FilterMeta> filterBy;

    private Map<String, ColumnMeta> columnMeta;

    private String width;

    public Map<String, SortMeta> getSortBy() {
        return sortBy;
    }

    public void setSortBy(Map<String, SortMeta> sortBy) {
        this.sortBy = sortBy;
    }

    public Map<String, FilterMeta> getFilterBy() {
        return filterBy;
    }

    public void setFilterBy(Map<String, FilterMeta> filterBy) {
        this.filterBy = filterBy;
    }

    public Map<String, ColumnMeta> getColumnMeta() {
        return columnMeta;
    }

    public void setColumnMeta(Map<String, ColumnMeta> columnMeta) {
        this.columnMeta = columnMeta;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
