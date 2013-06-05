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
package org.bonitasoft.connectors.googlecalendar.common;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gdata.util.AuthenticationException;
import org.bonitasoft.engine.connector.AbstractConnector;
import org.bonitasoft.engine.connector.ConnectorException;
import org.bonitasoft.engine.connector.ConnectorValidationException;

/**
 * @author Yanyan Liu
 */
public abstract class GoogleCalendarConnector extends AbstractConnector {

    // global input parameters
    private static final String USER_EMAIL = "userEmail";

    private static final String PASSWORD = "password";

    private static final String CALENDAR_URL = "calendarUrl";

    GoogleCalendarClient gcc;

    protected abstract void executeFunction(final GoogleCalendarClient gcc) throws ConnectorException;

    protected abstract List<String> validateExtraValues();

    @Override
    public void validateInputParameters() throws ConnectorValidationException {
        final List<String> errors = new ArrayList<String>(1);
        final String userEmail = (String) getInputParameter(USER_EMAIL);
        final String password = (String) getInputParameter(PASSWORD);
        final String calendarUrl = (String) getInputParameter(CALENDAR_URL);
        try {
            gcc = new GoogleCalendarClient(userEmail, password, calendarUrl);
        } catch (final AuthenticationException e) {
            errors.add("userEmail - User credentials not valid!");
            errors.add("password - User credentials not valid!");
        } catch (final MalformedURLException e) {
            errors.add("calendarURL - Malformed URL!");
        }
        errors.addAll(validateExtraValues());
        if (!errors.isEmpty()) {
            throw new ConnectorValidationException(this, errors);
        }
    }

    @Override
    protected void executeBusinessLogic() throws ConnectorException {
        executeFunction(gcc);
    }

}
