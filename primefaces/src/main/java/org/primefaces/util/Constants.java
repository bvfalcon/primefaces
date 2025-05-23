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
package org.primefaces.util;

public class Constants {

    public static class ContextParams {

        // Faces context params
        public static final String INTERPRET_EMPTY_STRING_AS_NULL = "jakarta.faces.INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL";

        // PF context params
        public static final String BEAN_VALIDATION_DISABLED = "jakarta.faces.validator.DISABLE_DEFAULT_BEAN_VALIDATOR";
        public static final String CACHE_PROVIDER = "primefaces.CACHE_PROVIDER";
        public static final String CLIENT_SIDE_LOCALISATION = "primefaces.CLIENT_SIDE_LOCALISATION";
        public static final String COOKIES_SAME_SITE = "primefaces.COOKIES_SAME_SITE";
        public static final String CSP = "primefaces.CSP";
        public static final String CSP_POLICY = "primefaces.CSP_POLICY";
        public static final String CSP_REPORT_ONLY_POLICY = "primefaces.CSP_REPORT_ONLY_POLICY";
        public static final String CSV = "primefaces.CLIENT_SIDE_VALIDATION";
        public static final String DIRECTION = "primefaces.DIR";
        public static final String EARLY_POST_PARAM_EVALUATION = "primefaces.EARLY_POST_PARAM_EVALUATION";
        public static final String EXCEPTION_TYPES_TO_IGNORE_IN_LOGGING = "primefaces.EXCEPTION_TYPES_TO_IGNORE_IN_LOGGING";
        public static final String FLEX = "primefaces.FLEX";
        public static final String HIDE_RESOURCE_VERSION = "primefaces.HIDE_RESOURCE_VERSION";
        public static final String INTERPOLATE_CLIENT_SIDE_VALIDATION_MESSAGES = "primefaces.INTERPOLATE_CLIENT_SIDE_VALIDATION_MESSAGES";
        public static final String MARK_INPUT_AS_INVALID_ON_ERROR_MSG = "primefaces.MARK_INPUT_AS_INVALID_ON_ERROR_MSG";
        public static final String MOVE_SCRIPTS_TO_BOTTOM = "primefaces.MOVE_SCRIPTS_TO_BOTTOM";
        public static final String MULTI_VIEW_STATE_STORE = "primefaces.MULTI_VIEW_STATE_STORE";
        public static final String PRIME_ICONS = "primefaces.PRIME_ICONS";
        public static final String RESET_VALUES = "primefaces.RESET_VALUES";
        public static final String SUBMIT = "primefaces.SUBMIT";
        public static final String THEME = "primefaces.THEME";
        public static final String TOUCHABLE = "primefaces.TOUCHABLE";
        public static final String TRANSFORM_METADATA = "primefaces.TRANSFORM_METADATA";
        public static final String DYNAMIC_CONTENT_LIMIT = "primefaces.DYNAMIC_CONTENT_LIMIT";

        private ContextParams() {

        }
    }

    public static class RequestParams {

        // Faces request params
        public static final String PARTIAL_REQUEST_PARAM = "jakarta.faces.partial.ajax";
        public static final String PARTIAL_UPDATE_PARAM = "jakarta.faces.partial.render";
        public static final String PARTIAL_PROCESS_PARAM = "jakarta.faces.partial.execute";
        public static final String PARTIAL_SOURCE_PARAM = "jakarta.faces.source";
        public static final String PARTIAL_BEHAVIOR_EVENT_PARAM = "jakarta.faces.behavior.event";
        public static final String RESET_VALUES_PARAM = "jakarta.faces.partial.resetValues";

        // PF request params
        public static final String IGNORE_AUTO_UPDATE_PARAM = "primefaces.ignoreautoupdate";
        public static final String SKIP_CHILDREN_PARAM = "primefaces.skipchildren";
        public static final String NONCE_PARAM = "primefaces.nonce";

        private RequestParams() {

        }
    }

    public static final String DOWNLOAD_COOKIE = "primefaces.download";

    public static final String LIBRARY = "primefaces";

    public static final String DYNAMIC_CONTENT_PARAM = "pfdrid";
    public static final String DYNAMIC_CONTENT_CACHE_PARAM = "pfdrid_c";
    public static final String DYNAMIC_CONTENT_TYPE_PARAM = "pfdrt";
    public static final String DYNAMIC_RESOURCES_MAPPING = "primefaces.dynamicResourcesMapping";

    public static final String BARCODE_MAPPING = "primefaces.barcodeMapping";

    public static final String FRAGMENT_PROCESS = "primefaces.fragment.process";
    public static final String FRAGMENT_UPDATE = "primefaces.fragment.update";

    public static class DialogFramework {

        public static final String OUTCOME = "dialog.outcome";
        public static final String OPTIONS = "dialog.options";
        public static final String PARAMS = "dialog.params";
        public static final String SOURCE_COMPONENT = "dialog.source.component";
        public static final String SOURCE_WIDGET = "dialog.source.widget";
        public static final String CONVERSATION_PARAM = "pfdlgcid";
        public static final String INCLUDE_VIEW_PARAMS = "includeViewParams";

        private DialogFramework() {

        }
    }

    public static final String SPACE = " ";

    public static final String EMPTY_STRING = "";

    public static final String COLON = ":";

    public static final String SEMICOLON = ";";

    public static final String CLIENT_BEHAVIOR_RENDERING_MODE = "CLIENT_BEHAVIOR_RENDERING_MODE";

    public static final String DEFAULT_CACHE_REGION = "primefaces.DEFAULT_CACHE_REGION";

    public static final String HELPER_RENDERER = "org.primefaces.HELPER_RENDERER";

    public static final String MULTI_VIEW_STATES = "primefaces.MULTI_VIEW_STATES";

    /** Space hack to fix Brazilian Real and other locale issues */
    public static final char NON_BREAKING_SPACE = '\u00A0';
    public static final String NON_BREAKING_SPACE_STR = Character.toString(NON_BREAKING_SPACE);

    private Constants() {
    }
}
