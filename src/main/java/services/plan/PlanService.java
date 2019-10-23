package services.plan;

import entities.Plan;

public interface PlanService {
    public int createPlan(Plan newPlan);
    public Plan searchPlanByPlanId(int plan_id);
    public Plan searchPlanByPostId(int post_id);
    public void updatePlan(int post_id, String meeting_point, String detail);
}
