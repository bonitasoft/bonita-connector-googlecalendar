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

import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarClient;
import org.bonitasoft.connectors.googlecalendar.common.GoogleCalendarConnector;
import org.bonitasoft.engine.connector.ConnectorException;

/**
 * @author Jordi Anguela, Yanyan Liu
 */
public class GoogleCalendarCreateEventWithWebContent extends GoogleCalendarConnector {

    // input parameters
    private static final String TITLE = "title";

    private static final String CONTENT = "content";

    private static final String START_TIME = "startTime";

    private static final String END_TIME = "endTime";

    private static final String IS_ALL_DAY = "isAllDay";

    private static final String ICON = "icon";

    private static final String ICON_TITLE = "iconTitle";

    private static final String CONTENT_TYPE = "contentType";

    private static final String URL = "url";

    private static final String WIDTH = "width";

    private static final String HEIGHT = "height";

    // output parameters
    protected static final String INSERTED_EDIT_LINK = "insertedEditLink";

    @Override
    protected void executeFunction(final GoogleCalendarClient gcc) throws ConnectorException {
        final String title = (String) getInputParameter(TITLE);
        final String content = (String) getInputParameter(CONTENT);
        final String startTime = (String) getInputParameter(START_TIME);
        final String endTime = (String) getInputParameter(END_TIME);
        final boolean isAllDay = (Boolean) getInputParameter(IS_ALL_DAY, false);
        final String icon = (String) getInputParameter(ICON);
        final String iconTitle = (String) getInputParameter(ICON_TITLE);
        final String contentType = (String) getInputParameter(CONTENT_TYPE);
        final String url = (String) getInputParameter(URL);
        final String width = (String) getInputParameter(WIDTH);
        final String height = (String) getInputParameter(HEIGHT);
        if (gcc != null) {
            String insertedEditLink = null;
            try {
                insertedEditLink = gcc.createEvenWithWebContent(title, content, startTime, endTime, isAllDay, icon, iconTitle, contentType, url, width, height,
                        null);
            } catch (final Exception ex) {
                ex.printStackTrace();
                throw new ConnectorException(ex);
            } finally {
                setOutputParameter(INSERTED_EDIT_LINK, insertedEditLink);
            }
        }
    }

    @Override
    protected List<String> validateExtraValues() {
        final List<String> errors = new ArrayList<String>(1);
        final String url = (String) getInputParameter(URL);
        if (url.length() == 0) {
            errors.add("url cannot be empty!");
        }
        final String width = (String) getInputParameter(WIDTH);
        if (width.length() == 0) {
            errors.add("width cannot be empty!");
        }
        final String height = (String) getInputParameter(HEIGHT);
        if (height.length() == 0) {
            errors.add("height cannot be empty!");
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
