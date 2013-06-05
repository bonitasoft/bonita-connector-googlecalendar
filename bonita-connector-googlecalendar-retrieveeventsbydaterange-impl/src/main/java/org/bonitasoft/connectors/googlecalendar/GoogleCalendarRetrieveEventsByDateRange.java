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

import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarClient;
import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarConnector;
import org.bonitasoft.engine.connector.ConnectorException;

/**
 * @author Haris Subasic, Yanyan Liu
 */
public class GoogleCalendarRetrieveEventsByDateRange extends GoogleCalendarConnector {

    // input parameters
    protected static final String START_TIME = "startTime";

    protected static final String END_TIME = "endTime";

    // output parameters
    protected static final String LIST_OF_EVENTS = "listOfEvents";

    protected static final String TITLE = "title";

    protected static final String CONTENT = "content";

    protected static final String EDIT_LINK = "editLink";

    @Override
    protected void executeFunction(final GoogleCalendarClient gcc) throws ConnectorException {
        if (gcc != null) {
            CalendarEventFeed resultFeed = null;
            final String startTime = (String) getInputParameter(START_TIME);
            final String endTime = (String) getInputParameter(END_TIME);
            try {
                resultFeed = gcc.retrieveEventsByDateRange(startTime, endTime);
            } catch (final Exception ex) {
                ex.printStackTrace();
                throw new ConnectorException(ex);
            } finally {
                if (resultFeed != null) {
                    final List<Map<String, String>> listOfEvents = new ArrayList<Map<String, String>>();
                    if (resultFeed.getEntries().size() > 0) {
                        for (final CalendarEventEntry eventEntry : resultFeed.getEntries()) {
                            final Map<String, String> oneEvent = new HashMap<String, String>();
                            // add event ID to the list
                            oneEvent.put(EDIT_LINK, eventEntry.getEditLink().getHref());
                            // add event title to the list (if it doesn't exist, add "" to the list)
                            if (eventEntry.getTitle() != null) {
                                oneEvent.put(TITLE, eventEntry.getTitle().getPlainText());
                            } else {
                                oneEvent.put(TITLE, "");
                            }
                            // add event content to the list (if it doesn't exist, add "" to the list)
                            if (eventEntry.getPlainTextContent() != null) {
                                oneEvent.put(CONTENT, eventEntry.getPlainTextContent());
                            } else {
                                oneEvent.put(CONTENT, "");
                            }
                            oneEvent.put(START_TIME, eventEntry.getTimes().get(0).getStartTime().toString());
                            oneEvent.put(END_TIME, eventEntry.getTimes().get(0).getEndTime().toString());

                            listOfEvents.add(oneEvent);
                        }
                    }
                    setOutputParameter(LIST_OF_EVENTS, listOfEvents);
                }
            }
        }
    }

    @Override
    protected List<String> validateExtraValues() {
        final List<String> errors = new ArrayList<String>(1);
        final String startTime = (String) getInputParameter(START_TIME);
        if (startTime == null || !(startTime.length() > 0)) {
            errors.add("startTime cannot be empty!");
        }
        final String endTime = (String) getInputParameter(END_TIME);
        if (endTime == null || !(endTime.length() > 0)) {
            errors.add("endTime cannot be empty!");
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
