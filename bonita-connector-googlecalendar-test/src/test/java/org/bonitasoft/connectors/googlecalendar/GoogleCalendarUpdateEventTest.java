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
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarConnector;
import org.bonitasoft.engine.connector.Connector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.bonitasoft.engine.test.annotation.Cover;
import org.bonitasoft.engine.test.annotation.Cover.BPMNConcept;
import org.junit.Test;

/**
 * @author Yanyan Liu
 */
public class GoogleCalendarUpdateEventTest extends GoogleCalendarConnectorTest {

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar" },
            story = "Check that edit link cannot be null.")
    @Test(expected = ConnectorValidationException.class)
    public void testEditLinkCannotBeNull() throws Exception {
        final HashMap<String, Object> parameters = new HashMap<String, Object>(1);
        final GoogleCalendarUpdateEvent connectorWithParams = (GoogleCalendarUpdateEvent) getGoogleCalendarConnector(parameters);
        connectorWithParams.setInputParameters(parameters);
        connectorWithParams.validateInputParameters();
    }

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar", "Update" },
            story = "Test event update.")
    @Test
    public void googleCalendarUpdateEvent() throws Exception {
        // create one event
        final String insertedEditLink = getInsertedEditLink();
        // do event update
        final HashMap<String, Object> parameters = new HashMap<String, Object>(5);
        parameters.put(GoogleCalendarUpdateEvent.EDIT_LINK, insertedEditLink);
        parameters.put(GoogleCalendarUpdateEvent.TITLE, "updated_title");
        parameters.put(GoogleCalendarUpdateEvent.CONTENT, "updated_content");
        parameters.put(GoogleCalendarUpdateEvent.START_TIME, "2012-12-25T16:00:00.000+01:00");
        parameters.put(GoogleCalendarUpdateEvent.END_TIME, "2012-12-27T17:00:00.000+01:00");
        final GoogleCalendarUpdateEvent connectorWithParams = (GoogleCalendarUpdateEvent) getGoogleCalendarConnector(parameters);
        connectorWithParams.validateInputParameters();
        Map<String, Object> result = connectorWithParams.execute();
        assertEquals(1, result.size());
        assertNotNull(result.get(GoogleCalendarUpdateEvent.UPDATED_TIME));
        // check updated event
        final GoogleCalendarRetrieveEvent googleCalendarRetrieveEventConnector = new GoogleCalendarRetrieveEvent();// getGoogleCalendarConnector(parameters);
        final Map<String, Object> defaultParameters = setGoogleCalendarConnectorParameters();
        defaultParameters.put(GoogleCalendarUpdateEvent.EDIT_LINK, insertedEditLink);
        googleCalendarRetrieveEventConnector.setInputParameters(defaultParameters);
        googleCalendarRetrieveEventConnector.validateInputParameters();
        result = googleCalendarRetrieveEventConnector.execute();
        assertEquals(4, result.size());
        assertEquals("updated_title", result.get(GoogleCalendarUpdateEvent.TITLE));
        assertEquals("updated_content", result.get(GoogleCalendarUpdateEvent.CONTENT));
        assertEquals("2012-12-25T16:00:00.000+01:00", result.get(GoogleCalendarUpdateEvent.START_TIME));
        assertEquals("2012-12-27T17:00:00.000+01:00", result.get(GoogleCalendarUpdateEvent.END_TIME));
        // delete event
        this.deleteEventByEditLink(insertedEditLink);
    }

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar", "Update" },
            story = "Test update event with bad edit link.")
    @Test(expected = ConnectorException.class)
    public void testUpdateEventWithBadEditLink() throws Exception {
        final HashMap<String, Object> parameters = new HashMap<String, Object>(1);
        final GoogleCalendarUpdateEvent connectorWithParams = (GoogleCalendarUpdateEvent) getGoogleCalendarConnector(parameters);
        parameters.put(GoogleCalendarUpdateEvent.EDIT_LINK, "bad_edit_link");
        parameters.put(GoogleCalendarUpdateEvent.TITLE, "updated_title");
        connectorWithParams.setInputParameters(parameters);
        connectorWithParams.validateInputParameters();
        connectorWithParams.execute();
    }

    @Override
    protected Class<? extends Connector> getConnectorClass() {
        return GoogleCalendarUpdateEvent.class;
    }

    @Override
    protected GoogleCalendarConnector getGoogleCalendarConnector(final Map<String, Object> parameters) throws Exception {
        final GoogleCalendarUpdateEvent connectorWithParams = new GoogleCalendarUpdateEvent();
        final Map<String, Object> defaultParameters = setGoogleCalendarConnectorParameters();
        defaultParameters.putAll(parameters);
        connectorWithParams.setInputParameters(defaultParameters);
        return connectorWithParams;
    }

}
