package tutor.cesh.filter;

/**
 * Behavior for filtering a list of tutors
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public interface TutorFilterable
{
    /**
     * Apply filters to a data set
     *
     * @param major Major filter
     * @param rate  Rate filter
     * @param year  Year filter
     */
    void applyFilters(String major, String rate, String year);
}
