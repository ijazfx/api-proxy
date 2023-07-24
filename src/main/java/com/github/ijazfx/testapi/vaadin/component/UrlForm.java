package com.github.ijazfx.testapi.vaadin.component;

import org.springframework.context.annotation.Scope;

import com.github.ijazfx.testapi.model.Url;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;

import io.graphenee.vaadin.flow.base.GxAbstractEntityForm;
import io.graphenee.vaadin.flow.component.FileUploader;

@SpringComponent
@Scope("prototype")
public class UrlForm extends GxAbstractEntityForm<Url> {

	private static final long serialVersionUID = 1L;

	private TextField title;

	private TextField url;

	private TextField alias;

	private FileUploader filePath;

	private PasswordField passphrase;

	private PasswordField confirmPassphrase;

	private Checkbox hasCertificate;

	private Checkbox isActive;

	public UrlForm() {
		super(Url.class);
	}

	@Override
	protected void decorateForm(HasComponents entityForm) {
		title = new TextField("Title");
		title.setMaxLength(100);
		alias = new TextField("Alias");
		alias.setMaxLength(50);
		url = new TextField("URL");
		url.setMaxLength(500);
		filePath = new FileUploader("Certificate/Keystore File");
		passphrase = new PasswordField("Passphrase");
		passphrase.setMaxLength(200);
		confirmPassphrase = new PasswordField("Confirm Passphrase");
		confirmPassphrase.setMaxLength(200);
		hasCertificate = new Checkbox("Has Certificate?");
		isActive = new Checkbox("Is Active?");

		FormLayout certificateLayout = new FormLayout();
		certificateLayout.setEnabled(false);
		certificateLayout.setResponsiveSteps(new ResponsiveStep("50px", 2));
		certificateLayout.add(filePath, passphrase, confirmPassphrase);

		certificateLayout.setColspan(filePath, 2);

		hasCertificate.addValueChangeListener(vcl -> {
			certificateLayout.setEnabled(vcl.getValue());
		});

		passphrase.addValueChangeListener(vcl -> {
			confirmPassphrase.setEnabled(vcl.getValue().length() >= 3);
		});

		confirmPassphrase.addValueChangeListener(vcl -> {
			if (!vcl.getValue().equals(passphrase.getValue())) {
				confirmPassphrase.setInvalid(true);
			} else {
				confirmPassphrase.setInvalid(false);
			}
		});

		confirmPassphrase.setErrorMessage("Passphrase and Confirm Passphrase do not match!");

		entityForm.add(title, alias, url, hasCertificate, certificateLayout, isActive);
		setColspan(url, 2);
		setColspan(certificateLayout, 2);
		setColspan(isActive, 2);
	}

	@Override
	protected void bindFields(Binder<Url> dataBinder) {
		dataBinder.forMemberField(title).asRequired();
		dataBinder.forMemberField(alias).asRequired();
		dataBinder.forMemberField(url).asRequired();
	}

}
