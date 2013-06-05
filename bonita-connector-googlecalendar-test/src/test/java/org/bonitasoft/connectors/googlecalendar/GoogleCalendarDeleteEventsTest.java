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

import java.util.HashMap;
import java.util.Map;

import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarConnector;
import org.bonitasoft.engine.connector.Connector;
import org.bonitasoft.engine.test.annotation.Cover;
import org.bonitasoft.engine.test.annotation.Cover.BPMNConcept;
import org.junit.Test;

/**
 * @author Yanyan Liu
 */

public class GoogleCalendarDeleteEventsTest extends GoogleCalendarConnectorTest {

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar", "Delete" },
            story = "Delete event works.")
    @Test
    public void deleteEventsWorksOk() throws Exception {
        final HashMap<String, Object> parameters = new HashMap<String, Object>(1);
        // create event first
        getInsertedEditLink();
        // delete event and check
        parameters.put(GoogleCalendarDeleteEvents.SEARCH_STRING, "Created");
        final GoogleCalendarDeleteEvents connectorWithParams = (GoogleCalendarDeleteEvents) getGoogleCalendarConnector(parameters);
        connectorWithParams.setInputParameters(parameters);
        connectorWithParams.validateInputParameters();
        final Map<String, Object> result = connectorWithParams.execute();
        assertEquals(0, result.size());
    }

    @Override
    protected Class<? extends Connector> getConnectorClass() {
        return GoogleCalendarDeleteEvents.class;
    }

    @Override
    protected GoogleCalendarConnector getGoogleCalendarConnector(final Map<String, Object> parameters) throws Exception {
        final GoogleCalendarDeleteEvents connectorWithParams = new GoogleCalendarDeleteEvents();
        final Map<String, Object> defaultParameters = setGoogleCalendarConnectorParameters();
        defaultParameters.putAll(parameters);
        connectorWithParams.setInputParameters(defaultParameters);
        return connectorWithParams;
    }

}
