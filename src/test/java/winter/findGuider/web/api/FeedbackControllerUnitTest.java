package winter.findGuider.web.api;

import entities.Feedback;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import services.Feedback.FeedbackService;

import static org.mockito.Mockito.when;

public class FeedbackControllerUnitTest {
    @InjectMocks
    FeedbackController feedbackController;

    @Mock
    FeedbackService feedbackService;

    @Rule
    public ExpectedException thrown= ExpectedException.none();
    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendFeedBack1(){
        Feedback feedback = Mockito.mock(Feedback.class);
        //when(feedbackService.sendFeedback(feedback)).thenReturn(true);
        feedbackController.sendFeedback(feedback);
    }
    @Test
    public void testSendFeedBack2() throws Exception {
        Feedback feedback = Mockito.mock(Feedback.class);
        when(feedbackService.sendFeedback(feedback)).thenReturn(true);
        feedbackController.sendFeedback(feedback);
    }

    @Test
    public void testSendFeedBack3() throws Exception {
        Feedback feedback = Mockito.mock(Feedback.class);
        when(feedbackService.sendFeedback(feedback)).thenReturn(true);
        when(feedbackService.createFeedback(feedback)).thenReturn(true);
        feedbackController.sendFeedback(feedback);
    }

    @Test(expected = AssertionError.class)
    public void testSendFeedBackWithException() throws Exception {
        thrown.expect(AssertionError.class);
        Feedback feedback = Mockito.mock(Feedback.class);

        when(feedbackService.sendFeedback(feedback)).thenThrow(Exception.class);
        //when(feedbackService.createFeedback(feedback)).thenThrow(Exception.class);
        feedbackController.sendFeedback(feedback);
    }
}
