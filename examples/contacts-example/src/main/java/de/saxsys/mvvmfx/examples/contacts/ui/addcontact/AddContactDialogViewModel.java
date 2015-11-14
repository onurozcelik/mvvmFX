package de.saxsys.mvvmfx.examples.contacts.ui.addcontact;

import de.saxsys.mvvmfx.InjectScope;
import de.saxsys.mvvmfx.ViewModel;
import de.saxsys.mvvmfx.examples.contacts.model.Contact;
import de.saxsys.mvvmfx.examples.contacts.model.Repository;
import de.saxsys.mvvmfx.examples.contacts.ui.contactdialog.ContactDialogViewModel;
import de.saxsys.mvvmfx.examples.contacts.ui.scopes.ContactDialogScope;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import javax.inject.Inject;
import java.util.ResourceBundle;

public class AddContactDialogViewModel implements ViewModel {
	static final String TITLE_LABEL_KEY = "dialog.addcontact.title";
	
	private final BooleanProperty dialogOpen = new SimpleBooleanProperty();
	
	@Inject
	private Repository repository;
	
	@InjectScope
	private ContactDialogScope dialogScope;
	
	@Inject
	private ResourceBundle defaultResourceBundle;
	
	public AddContactDialogViewModel() {
		dialogOpen.addListener((obs, oldV, newV) -> {
			if (!newV) {
				// contactDialogViewModel.resetDialogPage();
				dialogScope.publish(ContactDialogScope.Notifications.RESET_DIALOG_PAGE.toString());
                dialogScope.setContactToEdit(null);
			}
		});
	}
	
	
	public void setContactDialogViewModel(ContactDialogViewModel contactDialogViewModel) {
		contactDialogViewModel.setOkAction(this::addContactAction);
		contactDialogViewModel.titleTextProperty().set(defaultResourceBundle.getString(TITLE_LABEL_KEY));
	}
	
	
	public void addContactAction() {
		if (dialogScope.isContactFormValid()) {
			
			// contactDialogViewModel.getAddressFormViewModel().commitChanges();
			dialogScope.publish(ContactDialogScope.Notifications.COMMIT.toString());
			
			Contact contact = dialogScope.getContactToEdit();
			
			repository.save(contact);
			
			dialogOpen.set(false);
		}
	}
	
	public void openDialog() {
		// contactDialogViewModel.resetForms();
		dialogScope.publish(ContactDialogScope.Notifications.RESET_FORMS.toString());
		
		Contact contact = new Contact();
		dialogScope.setContactToEdit(contact);
		this.dialogOpenProperty().set(true);
	}
	
	
	public BooleanProperty dialogOpenProperty() {
		return dialogOpen;
	}
}
