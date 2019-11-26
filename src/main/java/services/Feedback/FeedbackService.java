package services.Feedback;

import entities.Feedback;

public interface FeedbackService {
    public boolean sendFeedback(Feedback feedback) throws Exception;

    public boolean createFeedback(Feedback newFeedback);
}
