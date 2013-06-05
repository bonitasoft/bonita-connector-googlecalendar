/**
 * Copyright (C) 2009-2012 BonitaSoft S.A.
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

import java.util.ArrayList;
import java.util.List;

import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.When;
import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarClient;
import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarConnector;
import org.bonitasoft.engine.connector.ConnectorException;

/**
 * @author Haris Subasic, Yanyan Liu
 */
public class GoogleCalendarRetrieveEvent extends GoogleCalendarConnector {

    // input parameters
    protected static final String EDIT_LINK = "editLink";

    // output parameters
    protected static final String TITLE = "title";

    protected static final String CONTENT = "content";

    protected static final String START_TIME = "startTime";

    protected static final String END_TIME = "endTime";

    private CalendarEventEntry event;

    @Override
    protected void executeFunction(final GoogleCalendarClient gcc) throws ConnectorException {
        if (gcc != null) {
            final String editLink = (String) getInputParameter(EDIT_LINK);
            try {
                event = gcc.retrieveEventByEditLink(editLink);
            } catch (final Exception ex) {
                ex.printStackTrace();
                throw new ConnectorException(ex);
            } finally {
                if (event != null) {
                    final String title = event.getTitle().getPlainText();
                    final String content = event.getPlainTextContent();
                    final When when = event.getTimes().get(0);
                    final DateTime startDateTime = when.getStartTime();
                    final String startTime = startDateTime.toString();
                    final DateTime endDateTime = when.getEndTime();
                    final String endTime = endDateTime.toString();
                    setOutputParameter(TITLE, title);
                    setOutputParameter(CONTENT, content);
                    setOutputParameter(START_TIME, startTime);
                    setOutputParameter(END_TIME, endTime);
                }
            }
        }
    }

    @Override
    protected List<String> validateExtraValues() {
        final List<String> errors = new ArrayList<String>(1);
        final String editLink = (String) getInputParameter(EDIT_LINK);
        if (editLink == null || !(editLink.length() > 0)) {
            errors.add("editLink cannot be empty!");
        }

        return errors;
    }

}
