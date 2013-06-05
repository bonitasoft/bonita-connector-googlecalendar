/**
 * Copyright (C) 2009 BonitaSoft S.A.
 * BonitaSoft, 31 rue Gustave Eiffel - 38000 Grenoble
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2.0 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.bonitasoft.connectors.googlecalendar.common;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gdata.client.Query;
import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.calendar.WebContent;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ServiceException;

/**
 * 
 * @author Jordi Anguela, Haris Subasic
 * 
 */
public class GoogleCalendarClient {

    private static final String TITLE = "title";

    private static final String CONTENT = "content";

    private static final String START_TIME = "startTime";

    private static final String END_TIME = "endTime";

    private static Logger LOGGER = Logger.getLogger(GoogleCalendarClient.class.getName());

    private CalendarService calendarService;

    private URL calendarURL;



    public static int compareDates(String startDate, String endDate) {
        DateTime startDateTime = DateTime.parseDateTime(startDate);
        DateTime endDateTime = DateTime.parseDateTime(endDate);
        return startDateTime.compareTo(endDateTime);
    }

    /**
     * Constructor
     * 
     * @param userEmail
     * @param password
     * @param url
     * @throws AuthenticationException
     * @throws MalformedURLException
     */
    public GoogleCalendarClient(String userEmail, String password, String url) throws AuthenticationException, MalformedURLException {

        // Create a new Calendar service
        calendarService = new CalendarService("My Application");
        calendarService.setUserCredentials(userEmail, password);

        calendarURL = new URL(url);
    }

    /**
     * Create a new Event in Google Calendar
     * 
     * @param title
     * @param content
     * @param startTime
     * @param endTime
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    public String createEvent(String title, String content, String startTime, String endTime, boolean isAllDay) throws IOException, ServiceException {

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Creating a new event : title='" + title
                    + "' content='" + content
                    + "' startTime='" + startTime
                    + "' endTime='" + endTime
                    + "' isAllDay='" + isAllDay + "'");
        }

        CalendarEventEntry newEvent = newEvent(title, content, startTime, endTime, isAllDay);
        // POST the request and receive the response:
        CalendarEventEntry insertedEntry = calendarService.insert(calendarURL, newEvent);
        return insertedEntry.getEditLink().getHref();
    }

    public String createEvenWithWebContent(String title,
            String content,
            String startTime,
            String endTime,
            boolean isAllDay,
            String icon,
            String iconTitle,
            String contentType,
            String url,
            String width,
            String height,
            Map<String, String> prefs) throws IOException, ServiceException {

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Creating a new event with web content : title='" + title
                    + "' content='" + content
                    + "' startTime='" + startTime
                    + "' endTime='" + endTime
                    + "' icon='" + icon
                    + "' iconTitle='" + iconTitle
                    + "' contentType='" + contentType
                    + "' url='" + url
                    + "' width='" + width
                    + "' height='" + height + "'");
        }

        CalendarEventEntry newEvent = newEvent(title, content, startTime, endTime, isAllDay);

        // Add WebContent information
        WebContent wc = new WebContent();
        wc.setIcon(icon);
        wc.setTitle(title);
        wc.setType(contentType);
        wc.setUrl(url);
        wc.setWidth(width);
        wc.setHeight(height);
        wc.setGadgetPrefs(prefs);

        newEvent.setWebContent(wc);

        // POST the request and receive the response:
        CalendarEventEntry insertedEntry = calendarService.insert(calendarURL, newEvent);
        return insertedEntry.getEditLink().getHref();
    }

    /**
     * Delete ALL events containing 'searchString' parameter in the title
     * 
     * @param searchString
     * @throws IOException
     * @throws ServiceException
     */
    public void deleteEvents(String searchString) throws IOException, ServiceException {

        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info("Deleting entries containing : '" + searchString + "'");
        }

        // Create a new query object and set the parameters
        Query myQuery = new Query(calendarURL);
        myQuery.setFullTextQuery(searchString);

        // Send the request with the built query URL
        CalendarEventFeed myResultsFeed = calendarService.query(myQuery, CalendarEventFeed.class);

        // Delete found entries
        if (myResultsFeed.getEntries().size() > 0) {
            for (CalendarEventEntry eventEntry : myResultsFeed.getEntries()) {
                if ((eventEntry.getTitle() != null) && (LOGGER.isLoggable(Level.INFO))) {
                    LOGGER.info("Event DELETED : '" + eventEntry.getTitle().getPlainText() + "'");
                }
                eventEntry.delete();
            }
        }
    }

    public void deleteEventByEditLink(String link) throws IOException, ServiceException {
        URL deleteUrl = new URL(link);
        try {
            calendarService.delete(deleteUrl, "*");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CalendarEventEntry retrieveEventByEditLink(String editLink) throws IOException, ServiceException {
        URL retrieveUrl = new URL(editLink);
        CalendarEventEntry myEvent = calendarService.getEntry(retrieveUrl, CalendarEventEntry.class);
        return myEvent;
    }

    public CalendarEventFeed retrieveEventsByDateRange(String startTime, String endTime) throws IOException, ServiceException {
        CalendarQuery myQuery = new CalendarQuery(calendarURL);
        myQuery.setMinimumStartTime(DateTime.parseDateTime(startTime));
        myQuery.setMaximumStartTime(DateTime.parseDateTime(endTime));

        CalendarEventFeed resultFeed = calendarService.query(myQuery, CalendarEventFeed.class);
        return resultFeed;
    }

    public String updateEvent(String editLink, Map<String, String> eventData) throws IOException, ServiceException {
        URL updateUrl = new URL(editLink);
        CalendarEventEntry myEvent = calendarService.getEntry(updateUrl, CalendarEventEntry.class);
        final String title = eventData.get(TITLE);
        final String content = eventData.get(CONTENT);
        final String startTime = eventData.get(START_TIME);
        final String endTime = eventData.get(END_TIME);
        if (title != null && title.length() > 0) {
            myEvent.setTitle(new PlainTextConstruct(title));
        }
        if (content != null && content.length() > 0) {
            myEvent.setContent(new PlainTextConstruct(content));
        }
        if (startTime != null && endTime != null) {
            DateTime sTime = DateTime.parseDateTime(startTime);
            DateTime eTime = DateTime.parseDateTime(endTime);
            myEvent.getTimes().get(0).setStartTime(sTime);
            myEvent.getTimes().get(0).setEndTime(eTime);
        }
        CalendarEventEntry updatedEntry = (CalendarEventEntry) calendarService.update(updateUrl, myEvent);
        String updatedTime = updatedEntry.getUpdated().toString();
        return updatedTime;
    }

    private CalendarEventEntry newEvent(String title, String content, String startTime, String endTime, boolean isAllDay) {
        CalendarEventEntry myEvent = new CalendarEventEntry();

        // Set the title and description
        myEvent.setTitle(new PlainTextConstruct(title));
        myEvent.setContent(new PlainTextConstruct(content));

        // Create DateTime events and create a When object to hold them, then add
        // the When event to the event
        DateTime sTime = DateTime.parseDateTime(startTime);
        DateTime eTime = DateTime.parseDateTime(endTime);

        // Check if event is all day
        if (isAllDay)
        {
            sTime.setDateOnly(true);
            eTime.setDateOnly(true);
        }

        When eventTimes = new When();
        eventTimes.setStartTime(sTime);
        eventTimes.setEndTime(eTime);
        myEvent.addTime(eventTimes);

        return myEvent;
    }

    public static Boolean isDateWellFormed(String date) {
        try {
            DateTime.parseDateTime(date);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
