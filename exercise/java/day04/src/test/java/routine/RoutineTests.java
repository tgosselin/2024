package routine;

import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;

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
        ReindeerFeederTest reindeerFeeder = new ReindeerFeederTest();
        EmailServiceTest emailService = new EmailServiceTest(reindeerFeeder);
        ScheduleServiceTest scheduleService = new ScheduleServiceTest(emailService);

        Routine routine = new Routine(emailService, scheduleService, reindeerFeeder);
        routine.start();

        assertThat(scheduleService.isRoutineDone()).isTrue();
    }

    private static class EmailServiceTest implements EmailService {
        private final ReindeerFeederTest reindeerFeeder;
        private boolean emailsRead = false;

        public EmailServiceTest(ReindeerFeederTest reindeerFeeder) {
            this.reindeerFeeder = reindeerFeeder;
        }

        @Override
        public void readNewEmails() {
            if (!reindeerFeeder.hasBeenFed()) {
                throw new IllegalArgumentException("Reindeer feeder has not been fed before reading emails");
            }
            emailsRead = true;
        }

        public boolean hasReadEmails() {
            return emailsRead;
        }
    }

    private static class ScheduleServiceTest implements ScheduleService {

        private final EmailServiceTest emailService;
        private Schedule todaySchedule = null;
        private boolean routineDone = false;

        public ScheduleServiceTest(EmailServiceTest emailService) {
            this.emailService = emailService;
        }

        @Override
        public Schedule todaySchedule() {
            todaySchedule = new Schedule();
            return todaySchedule;
        }

        @Override
        public void organizeMyDay(Schedule schedule) {
            if (todaySchedule == null || todaySchedule != schedule) {
                throw new IllegalArgumentException("Schedule must be retrieved before organizing the day");
            }
        }

        @Override
        public void continueDay() {
            if (!emailService.hasReadEmails()) {
                throw new IllegalArgumentException("New emails have not been read before continuing the day");
            }
            routineDone = true;
        }

        public boolean isRoutineDone() {
            return routineDone;
        }
    }

    private static class ReindeerFeederTest implements ReindeerFeeder {
        private boolean hasBeenFed = false;

        @Override
        public void feedReindeers() {
            hasBeenFed = true;
        }

        public boolean hasBeenFed() {
            return hasBeenFed;
        }
    }
}