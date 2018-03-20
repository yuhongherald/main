package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Employee;
import seedu.address.testutil.EmployeeBuilder;

/**
 * Contains integration tests (interaction with the Model) for {@code AddEmployeeCommand}.
 */
public class AddEmployeeCommandIntegrationTest {

    private Model model;

    @Before
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_newPerson_success() throws Exception {
        Employee validEmployee = new EmployeeBuilder().build();

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.addPerson(validEmployee);

        assertCommandSuccess(prepareCommand(validPerson, model), model,
                String.format(AddEmployeeCommand.MESSAGE_SUCCESS, validPerson), expectedModel);
    }

    @Test
    public void execute_duplicatePerson_throwsCommandException() {
        Person personInList = model.getAddressBook().getPersonList().get(0);
        assertCommandFailure(prepareCommand(personInList, model), model, AddEmployeeCommand.MESSAGE_DUPLICATE_PERSON);
    }

    /**
     * Generates a new {@code AddEmployeeCommand} which upon execution, adds {@code person} into the {@code model}.
     */
    private AddEmployeeCommand prepareCommand(Person person, Model model) {
        AddEmployeeCommand command = new AddEmployeeCommand(person);
        command.setData(model, new CommandHistory(), new UndoRedoStack());
        return command;
    }
}
