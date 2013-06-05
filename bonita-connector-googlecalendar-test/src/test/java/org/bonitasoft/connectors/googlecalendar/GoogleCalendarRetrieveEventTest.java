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
public class GoogleCalendarRetrieveEventTest extends GoogleCalendarConnectorTest {

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar" },
            story = "Check that edit link cannot be null.")
    @Test(expected = ConnectorValidationException.class)
    public void testEditLinkCannotBeNull() throws Exception {
        final HashMap<String, Object> parameters = new HashMap<String, Object>(2);
        final GoogleCalendarRetrieveEvent connectorWithParams = (GoogleCalendarRetrieveEvent) getGoogleCalendarConnector(parameters);
        connectorWithParams.setInputParameters(parameters);
        connectorWithParams.validateInputParameters();
    }

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar", "Retrieve" },
            story = "Test retrieve event works.")
    @Test
    public void googleCalendarRetrieveEventWorksOk() throws Exception {
        final HashMap<String, Object> parameters = new HashMap<String, Object>(1);
        // create one event
        final String insertedEditLink = getInsertedEditLink();
        // do event retrieve and check
        parameters.put(GoogleCalendarRetrieveEvent.EDIT_LINK, insertedEditLink);
        final GoogleCalendarRetrieveEvent connectorWithParams = (GoogleCalendarRetrieveEvent) getGoogleCalendarConnector(parameters);
        connectorWithParams.setInputParameters(parameters);
        connectorWithParams.validateInputParameters();
        final Map<String, Object> result = connectorWithParams.execute();
        assertEquals(4, result.size());
        assertNotNull(result.get(GoogleCalendarRetrieveEvent.TITLE));
        assertNotNull(result.get(GoogleCalendarRetrieveEvent.CONTENT));
        assertNotNull(result.get(GoogleCalendarRetrieveEvent.START_TIME));
        assertNotNull(result.get(GoogleCalendarRetrieveEvent.END_TIME));
        // delete event
        this.deleteEventByEditLink(insertedEditLink);
    }

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar", "Retrieve" },
            story = "Test retrieve event with bad edit link.")
    @Test(expected = ConnectorException.class)
    public void testRetrieveEventWithBadEditLink() throws Exception {
        final HashMap<String, Object> parameters = new HashMap<String, Object>(1);
        final GoogleCalendarRetrieveEvent connectorWithParams = (GoogleCalendarRetrieveEvent) getGoogleCalendarConnector(parameters);
        parameters.put(GoogleCalendarRetrieveEvent.EDIT_LINK, "bad_edit_link");
        connectorWithParams.setInputParameters(parameters);
        connectorWithParams.validateInputParameters();
        connectorWithParams.execute();
    }

    @Override
    protected Class<? extends Connector> getConnectorClass() {
        return GoogleCalendarRetrieveEvent.class;
    }

    @Override
    protected GoogleCalendarConnector getGoogleCalendarConnector(final Map<String, Object> parameters) throws Exception {
        final GoogleCalendarRetrieveEvent connectorWithParams = new GoogleCalendarRetrieveEvent();
        final Map<String, Object> defaultParameters = setGoogleCalendarConnectorParameters();
        defaultParameters.putAll(parameters);
        connectorWithParams.setInputParameters(defaultParameters);
        return connectorWithParams;
    }

}
