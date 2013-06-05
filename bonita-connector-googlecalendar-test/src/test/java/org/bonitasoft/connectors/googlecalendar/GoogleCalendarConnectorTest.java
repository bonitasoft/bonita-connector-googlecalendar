/**
 * Copyright (C) 2012 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.googlecalendar;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarConnector;
import org.bonitasoft.engine.connector.Connector;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.bonitasoft.engine.test.annotation.Cover;
import org.bonitasoft.engine.test.annotation.Cover.BPMNConcept;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

/**
 * @author Yanyan Liu
 */
public abstract class GoogleCalendarConnectorTest {

    protected static final Logger LOG = Logger.getLogger(GoogleCalendarConnectorTest.class.getName());

    @Rule
    public TestRule testWatcher = new TestWatcher() {

        @Override
        public void starting(final Description d) {
            LOG.warning("==== Starting test: " + this.getClass().getName() + "." + d.getMethodName() + "() ====");
        }

        @Override
        public void failed(final Throwable e, final Description d) {
            LOG.warning("==== Failed test: " + this.getClass().getName() + "." + d.getMethodName() + "() ====");
        }

        @Override
        public void succeeded(final Description d) {
            LOG.warning("==== Succeeded test: " + this.getClass().getName() + "." + d.getMethodName() + "() ====");
        }

    };

    private String userEmail;

    private String password;

    private String calendarUrl;

    private final static Date TIME = new Date();

    private final String title = "Created on " + new java.sql.Timestamp(TIME.getTime());

    private String content;

    private String startTime;

    private String endTime;

    private boolean isAllDay;

    private String icon;

    private String iconTitle;

    private String contentType;

    private String url;

    private String width;

    private String height;

    private static final String USER_EMAIL = "userEmail";

    private static final String PASSWORD = "password";

    private static final String CALENDAR_URL = "calendarUrl";

    private static final String TITLE = "title";

    private static final String CONTENT = "content";

    private static final String START_TIME = "startTime";

    private static final String END_TIME = "endTime";

    private static final String IS_ALL_DAY = "isAllDay";

    private static final String ICON = "icon";

    private static final String ICON_TITLE = "iconTitle";

    protected static final String CONTENT_TYPE = "contentType";

    private static final String URL = "url";

    private static final String WIDTH = "width";

    private static final String HEIGHT = "height";

    public Map<String, Object> setGoogleCalendarConnectorParameters() throws Exception {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        getProperties();
        parameters.put(USER_EMAIL, userEmail);
        parameters.put(PASSWORD, password);
        parameters.put(CALENDAR_URL, calendarUrl);
        parameters.put(TITLE, title);
        parameters.put(CONTENT, content);
        parameters.put(START_TIME, startTime);
        parameters.put(END_TIME, endTime);
        parameters.put(IS_ALL_DAY, isAllDay);
        parameters.put(ICON, icon);
        parameters.put(ICON_TITLE, iconTitle);
        parameters.put(CONTENT_TYPE, contentType);
        parameters.put(URL, url);
        parameters.put(WIDTH, width);
        parameters.put(HEIGHT, height);
        return parameters;
    }

    private void getProperties() throws Exception {
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/test/resources/googleCalendar.properties"));
        userEmail = prop.getProperty(USER_EMAIL);
        password = prop.getProperty(PASSWORD);
        calendarUrl = prop.getProperty(CALENDAR_URL);
        content = prop.getProperty(CONTENT);
        startTime = prop.getProperty(START_TIME);
        endTime = prop.getProperty(END_TIME);
        isAllDay = Boolean.parseBoolean(prop.getProperty(IS_ALL_DAY));
        icon = prop.getProperty(ICON);
        iconTitle = prop.getProperty(ICON_TITLE);
        contentType = prop.getProperty("CONTENT_TYPE");
        url = prop.getProperty(URL);
        width = prop.getProperty(WIDTH);
        height = prop.getProperty(HEIGHT);
    }

    protected abstract Class<? extends Connector> getConnectorClass();

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar" },
            story = "Test exception when user email is empty.")
    @Test(expected = ConnectorValidationException.class)
    public void testEmptyUserEmail() throws Exception {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(USER_EMAIL, "");
        final GoogleCalendarConnector connector = getGoogleCalendarConnector(parameters);
        connector.setInputParameters(parameters);
        connector.validateInputParameters();
        // test null userEmail
        parameters.put(USER_EMAIL, null);
        connector.setInputParameters(parameters);
        connector.validateInputParameters();
        // test empty userEmail
        parameters.put(USER_EMAIL, "   ");
        connector.setInputParameters(parameters);
        connector.validateInputParameters();
    }

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar" },
            story = "Test exception when password is empty.")
    @Test(expected = ConnectorValidationException.class)
    public void testEmptyPassword() throws Exception {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(PASSWORD, "");
        final GoogleCalendarConnector connector = getGoogleCalendarConnector(parameters);
        connector.setInputParameters(parameters);
        connector.validateInputParameters();
        // test null password
        parameters.put(PASSWORD, null);
        connector.setInputParameters(parameters);
        connector.validateInputParameters();
    }

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar" },
            story = "Test calendar url.")
    @Test(expected = ConnectorValidationException.class)
    public void testCalendarUrl() throws Exception {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        final GoogleCalendarConnector connector = getGoogleCalendarConnector(parameters);
        // test empty calendarUrl
        parameters.put(CALENDAR_URL, "");
        connector.setInputParameters(parameters);
        connector.validateInputParameters();
        // test null calendarUrl
        parameters.put(CALENDAR_URL, null);
        connector.setInputParameters(parameters);
        connector.validateInputParameters();
    }

    protected abstract GoogleCalendarConnector getGoogleCalendarConnector(Map<String, Object> parameters) throws Exception;

    public String getInsertedEditLink() throws Exception {
        final GoogleCalendarCreateEvent connectorWithParams = new GoogleCalendarCreateEvent();
        final Map<String, Object> defaultParameters = setGoogleCalendarConnectorParameters();
        connectorWithParams.setInputParameters(defaultParameters);
        connectorWithParams.validateInputParameters();
        final Map<String, Object> result = connectorWithParams.execute();
        return (String) result.get(GoogleCalendarCreateEvent.INSERTED_EDIT_LINK);
    }

    public void deleteEventByEditLink(final String insertedEditLink) throws Exception {
        final HashMap<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put("editLink", insertedEditLink);
        final Map<String, Object> defaultParameters = setGoogleCalendarConnectorParameters();
        defaultParameters.putAll(parameters);
        final GoogleCalendarDeleteEventByEditLink deleteEventByEditLinkConnector = new GoogleCalendarDeleteEventByEditLink();
        deleteEventByEditLinkConnector.setInputParameters(defaultParameters);
        deleteEventByEditLinkConnector.validateInputParameters();
        final Map<String, Object> result = deleteEventByEditLinkConnector.execute();
        // no output
        assertEquals(0, result.size());
    }
}
