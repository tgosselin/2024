package routine;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

class RoutineTests {
    @Test
    void startRoutineWithMockito() {
        // Write a test using Mockito
        EmailService emailService = Mockito.mock(EmailService.class);
        ScheduleService scheduleService = Mockito.mock(ScheduleService.class);
        Schedule schedule = new Schedule();
        Mockito.when(scheduleService.todaySchedule()).thenReturn(schedule);
        ReindeerFeeder reindeerFeeder = Mockito.mock(ReindeerFeeder.class);

        Routine routine = new Routine(emailService, scheduleService, reindeerFeeder);
        routine.start();

        InOrder inOrder = Mockito.inOrder(scheduleService, reindeerFeeder, emailService);
        inOrder.verify(scheduleService).organizeMyDay(schedule);
        inOrder.verify(reindeerFeeder).feedReindeers();
        inOrder.verify(emailService).readNewEmails();
        inOrder.verify(scheduleService).continueDay();
    }

    @Test
    void startRoutineWithManualTestDoubles() {
        // Write a test using your own test double(s)
        
    }
}