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
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.test.annotation.Cover;
import org.bonitasoft.engine.test.annotation.Cover.BPMNConcept;
import org.junit.Test;

/**
 * @author Yanyan Liu
 */
public class GoogleCalendarDeleteEventByEditLinkTest extends GoogleCalendarConnectorTest {

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar", "Delete" },
            story = "Delete event by edit link.")
    @Test
    public void testDeleteEventByEditLinkOk() throws Exception {
        final HashMap<String, Object> parameters = new HashMap<String, Object>(1);
        final String insertedEditLink = getInsertedEditLink();
        parameters.put(GoogleCalendarDeleteEventByEditLink.EDIT_LINK, insertedEditLink);
        final GoogleCalendarDeleteEventByEditLink connectorWithParams = (GoogleCalendarDeleteEventByEditLink) getGoogleCalendarConnector(parameters);
        connectorWithParams.validateInputParameters();
        final Map<String, Object> result = connectorWithParams.execute();
        // no output
        assertEquals(0, result.size());
    }

    @Cover(classes = { GoogleCalendarCreateEvent.class }, concept = BPMNConcept.CONNECTOR, keywords = { "Google calendar", "Delete" },
            story = "Delete event by edit link with bad link.")
    @Test(expected = ConnectorException.class)
    public void testDeleteEventWithBadEditLink() throws Exception {
        final HashMap<String, Object> parameters = new HashMap<String, Object>(1);
        final GoogleCalendarDeleteEventByEditLink connectorWithParams = (GoogleCalendarDeleteEventByEditLink) getGoogleCalendarConnector(parameters);
        parameters.put(GoogleCalendarDeleteEventByEditLink.EDIT_LINK, "bad_edit_link");
        connectorWithParams.setInputParameters(parameters);
        connectorWithParams.validateInputParameters();
        connectorWithParams.execute();
    }

    @Override
    protected Class<? extends Connector> getConnectorClass() {
        return GoogleCalendarDeleteEventByEditLink.class;
    }

    @Override
    protected GoogleCalendarConnector getGoogleCalendarConnector(final Map<String, Object> parameters) throws Exception {
        final GoogleCalendarDeleteEventByEditLink connectorWithParams = new GoogleCalendarDeleteEventByEditLink();
        final Map<String, Object> defaultParameters = setGoogleCalendarConnectorParameters();
        defaultParameters.putAll(parameters);
        connectorWithParams.setInputParameters(defaultParameters);
        return connectorWithParams;
    }

}
