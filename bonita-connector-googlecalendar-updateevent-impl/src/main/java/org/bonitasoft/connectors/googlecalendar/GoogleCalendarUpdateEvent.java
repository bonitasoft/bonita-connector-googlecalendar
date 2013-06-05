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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarClient;
import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarConnector;
import org.bonitasoft.engine.connector.ConnectorException;

/**
 * @author Haris Subasic, Yanyan Liu
 */
public class GoogleCalendarUpdateEvent extends GoogleCalendarConnector {

    // input parameters
    protected static final String EDIT_LINK = "editLink";

    protected static final String TITLE = "title";

    protected static final String CONTENT = "content";

    protected static final String START_TIME = "startTime";

    protected static final String END_TIME = "endTime";

    // output parameters
    protected static final String UPDATED_TIME = "updatedTime";

    @Override
    protected void executeFunction(final GoogleCalendarClient gcc) throws ConnectorException {
        if (gcc != null) {
            final Map<String, String> eventData = new HashMap<String, String>();
            final String title = (String) getInputParameter(TITLE);
            final String content = (String) getInputParameter(CONTENT);
            final String startTime = (String) getInputParameter(START_TIME);
            final String endTime = (String) getInputParameter(END_TIME);
            eventData.put(TITLE, title);
            eventData.put(CONTENT, content);
            eventData.put(START_TIME, startTime);
            eventData.put(END_TIME, endTime);
            final String editLink = (String) getInputParameter(EDIT_LINK);
            String updatedTime = null;
            try {
                updatedTime = gcc.updateEvent(editLink, eventData);
            } catch (final Exception ex) {
                ex.printStackTrace();
                throw new ConnectorException(ex);
            } finally {
                this.setOutputParameter(UPDATED_TIME, updatedTime);
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

        String startDate = (String) getInputParameter(START_TIME);
        String endDate = (String) getInputParameter(END_TIME);
        Boolean startDateWellFormed = GoogleCalendarClient.isDateWellFormed(startDate);
        Boolean endDateWellFormed = GoogleCalendarClient.isDateWellFormed(endDate);

        if (!startDateWellFormed) {
            errors.add("Start date not well formed");
        }

        if (!endDateWellFormed) {
            errors.add("End date not well formed");
        }

        if (startDateWellFormed && endDateWellFormed) {
            int dateComparison = GoogleCalendarClient.compareDates(startDate, endDate);
            if (dateComparison == 0) {
                errors.add("Dates must be different");
            }
            if (dateComparison > 0) {
                errors.add("Start time must be before end time");
            }
        }


        return errors;
    }

}
