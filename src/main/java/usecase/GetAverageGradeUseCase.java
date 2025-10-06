package usecase;

import api.GradeDataBase;
import entity.Grade;
import entity.Team;

/**
 * GetAverageGradeUseCase class.
 */
public final class GetAverageGradeUseCase {
    private final GradeDataBase gradeDataBase;

    public GetAverageGradeUseCase(GradeDataBase gradeDataBase) {
        this.gradeDataBase = gradeDataBase;
    }

    /**
     * Get the average grade for a course across your team.
     * @param course The course.
     * @return The average grade.
     */
    public float getAverageGrade(String course) {
        // Call the API to get usernames of all your team members
        float sum = 0;
        int count = 0;

        final Team team = gradeDataBase.getMyTeam();
        // Call the API to get all the grades for the course for all your team members

        if (team == null || team.getMembers() == null) {
            return 0;  // default case
        }

        // recursive all the teammates
        for (String username : team.getMembers()) {
            try {
                // call API to get the all course grade from teammate
                Grade[] grades = gradeDataBase.getGrades(username);

                // recursive all the grades array from course
                for (Grade g : grades) {
                    // find the grade that match the course number
                    if (g.getCourse().equalsIgnoreCase(course)) {
                        sum += g.getGrade();
                        count++;
                    }
                }
            } catch (RuntimeException e) {
                // avoid error
            }
        }

        if (count == 0) {
            return 0;
        }
        return sum / count;
    }
}
