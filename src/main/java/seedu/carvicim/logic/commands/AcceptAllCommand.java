package seedu.carvicim.logic.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import seedu.carvicim.logic.commands.exceptions.CommandException;
import seedu.carvicim.model.job.Job;
import seedu.carvicim.storage.session.ImportSession;
import seedu.carvicim.storage.session.exceptions.DataIndexOutOfBoundsException;
import seedu.carvicim.storage.session.exceptions.UnitializedException;

//@@author yuhongherald

/**
 * Accepts all remaining unreviewed job entries into Servicing Manager
 */
public class AcceptAllCommand extends Command {

    public static final String COMMAND_WORD = "acceptAll";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Accepts all unreviewed job entries. "
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "%d job entries accepted!";

    public String getMessageSuccess(int entries) {
        return String.format(MESSAGE_SUCCESS, entries);
    }

    @Override
    public CommandResult execute() throws CommandException {
        ImportSession importSession = ImportSession.getInstance();
        if (importSession.getSessionData().getUnreviewedJobEntries().isEmpty()) {
            throw new CommandException("There are no job entries to review!");
        }
        try {
            importSession.reviewAllRemainingJobEntries(true);
            List<Job> jobs = new ArrayList<>(importSession.getSessionData().getReviewedJobEntries());
            model.addJobs(jobs);
            importSession.closeSession();
            return new CommandResult(getMessageSuccess(jobs.size()));
        } catch (DataIndexOutOfBoundsException e) {
            throw new CommandException("Excel file has bad format. Try copying the cell values into a new excel file "
                    + "before trying again");
        } catch (IOException e) {
            throw new CommandException("Unable to export file. Please close the application and try again.");
        } catch (UnitializedException e) {
            throw new CommandException(e.getMessage());
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AcceptAllCommand); // instanceof handles nulls
    }

}
