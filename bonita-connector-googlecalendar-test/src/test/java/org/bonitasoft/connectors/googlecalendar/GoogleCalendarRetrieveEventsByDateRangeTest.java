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
import java.util.List;
import java.util.Map;

import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarConnector;
import org.bonitasoft.engine.connector.Connector;
import org.bonitasoft.engine.connector.ConnectorValidationException;
import org.bonitasoft.engine.test.annotation.Cover;
import org.bonitasoft.engine.test.annotation.Cover.BPMNConcept;
import org.junit.Test;

/**
 * @author Yanyan Liu
 */
public class GoogleCalendarRetrieveEventsByDateRangeTest extends GoogleCalendarConnectorTest {

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar", "Start time" },
            story = "Check that start time cannot be null.")
    @Test(expected = ConnectorValidationException.class)
    public void testStartTimeCannotBeNull() throws Exception {
        final HashMap<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put(GoogleCalendarRetrieveEventsByDateRange.START_TIME, null);
        final GoogleCalendarRetrieveEventsByDateRange connectorWithParams = (GoogleCalendarRetrieveEventsByDateRange) getGoogleCalendarConnector(parameters);
        connectorWithParams.validateInputParameters();
    }

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar", "End time" },
            story = "Check that end time cannot be null.")
    @Test(expected = ConnectorValidationException.class)
    public void testEndTimeCannotBeNull() throws Exception {
        final HashMap<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put(GoogleCalendarRetrieveEventsByDateRange.END_TIME, null);
        final GoogleCalendarRetrieveEventsByDateRange connectorWithParams = (GoogleCalendarRetrieveEventsByDateRange) getGoogleCalendarConnector(parameters);
        connectorWithParams.validateInputParameters();
    }

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar", "Retrieve" },
            story = "Test retrieve events by date range.")
    @SuppressWarnings("unchecked")
    @Test
    public void googleCalendarRetrieveEventsByDateRangeWorksOk() throws Exception {
        // get and record number of existing event
        final HashMap<String, Object> parameters = new HashMap<String, Object>(2);
        parameters.put(GoogleCalendarRetrieveEventsByDateRange.START_TIME, "2013-02-06T16:00:00.000+01:00");
        parameters.put(GoogleCalendarRetrieveEventsByDateRange.END_TIME, "2013-02-08T17:00:00.000+01:00");
        final GoogleCalendarRetrieveEventsByDateRange connectorWithParams = (GoogleCalendarRetrieveEventsByDateRange) getGoogleCalendarConnector(parameters);
        connectorWithParams.validateInputParameters();
        Map<String, Object> result = connectorWithParams.execute();
        List<Map<String, String>> listOfEvents = (List<Map<String, String>>) result.get(GoogleCalendarRetrieveEventsByDateRange.LIST_OF_EVENTS);
        final int existingCount = listOfEvents.size();
        // create a new event
        final String insertedEditLink = getInsertedEditLink();
        // retrieve again and check result
        result = connectorWithParams.execute();
        assertNotNull(result.get(GoogleCalendarRetrieveEventsByDateRange.LIST_OF_EVENTS));
        listOfEvents = (List<Map<String, String>>) result.get(GoogleCalendarRetrieveEventsByDateRange.LIST_OF_EVENTS);
        assertEquals(existingCount + 1, listOfEvents.size());
        final Map<String, String> event = listOfEvents.get(result.size() - 1);
        assertEquals(insertedEditLink, event.get(GoogleCalendarRetrieveEventsByDateRange.EDIT_LINK));

        this.deleteEventByEditLink(insertedEditLink);
    }

    @Override
    protected Class<? extends Connector> getConnectorClass() {
        return GoogleCalendarRetrieveEventsByDateRange.class;
    }

    @Override
    protected GoogleCalendarConnector getGoogleCalendarConnector(final Map<String, Object> parameters) throws Exception {
        final GoogleCalendarRetrieveEventsByDateRange connectorWithParams = new GoogleCalendarRetrieveEventsByDateRange();
        final Map<String, Object> defaultParameters = setGoogleCalendarConnectorParameters();
        defaultParameters.putAll(parameters);
        connectorWithParams.setInputParameters(defaultParameters);
        return connectorWithParams;
    }

}
