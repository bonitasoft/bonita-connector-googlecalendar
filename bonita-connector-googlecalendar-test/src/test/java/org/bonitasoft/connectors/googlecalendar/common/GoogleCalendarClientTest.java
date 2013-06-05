package org.bonitasoft.connectors.googlecalendar.common;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class GoogleCalendarClientTest {
    @Test
    public void testCompareDates() throws Exception {
        String date1 = "2012-12-25T20:00:00+01:00";
        String date2 = "2012-12-25T23:00:00+01:00";
        assertThat(GoogleCalendarClient.compareDates(date1, date2), lessThan(0));
    }

    @Test
    public void testCompareSameDates() throws Exception {
        String date1 = "2012-12-25T20:00:00+01:00";
        String date2 = "2012-12-25T20:00:00+01:00";
        assertThat(GoogleCalendarClient.compareDates(date1, date2), is(0));
    }

    @Test
    public void testCompareSameDatesDifferentTimezones() throws Exception {
        String date1 = "2012-12-25T20:00:00+00:00";
        String date2 = "2012-12-25T21:00:00+01:00";
        assertThat(GoogleCalendarClient.compareDates(date1, date2), is(0));
    }

    @Test
    public void testDateNotWellFormed() throws Exception {
        String date = "blabla";
        assertThat(GoogleCalendarClient.isDateWellFormed(date), is(false));
    }

    @Test
    public void testDateWellFormed() throws Exception {
        String date = "2012-12-25T20:00:00+01:00";
        assertThat(GoogleCalendarClient.isDateWellFormed(date), is(true));
    }

    @Test
    public void testNullDate() throws Exception {
        String date = null;
        assertThat(GoogleCalendarClient.isDateWellFormed(date), is(false));
    }

    @Test
    public void testEmptyDate() throws Exception {
        String date = "";
        assertThat(GoogleCalendarClient.isDateWellFormed(date), is(false));
    }
}
