package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Department entity;

	private DepartmentService service;
	
	private List<DataChangeListener> dataChangeListener = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	@FXML
	private TextField txtName;
	@FXML
	private Label labelErroName;
	@FXML
	private Button btSave;
	@FXML
	private Button btCancel;

	public void setDepartment(Department entity) {
		this.entity = entity;
	}

	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}

	@FXML
	public void onbtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity é nulo!!");
		}

		if (service == null) {
			throw new IllegalStateException("service é nulo!!");
		}
		try {
			entity = getFormData();
			
			service.saveOrUpdate(entity);
			notifyDataChangeListener();
			Utils.currentStage(event).close();
		}catch(ValidationException e) {
			setErrorsMessage(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error ao salvar objeto", null, e.getMessage(), Alert.AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListener() {
		for(DataChangeListener listener : dataChangeListener) {
			listener.onDateChanged();
		}
		
	}

	public void subScribeDataChangeListener(DataChangeListener listener) {
		dataChangeListener.add(listener);
	}

	private Department getFormData() {
		Department obj = new Department();
		
		ValidationException valid = new ValidationException("Validation error!");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if(txtName.getText() == null || txtName.getText().trim().equals("")) {
			valid.addError("name", "*Campo name não pode ser vazio.");
		}
		obj.setName(txtName.getText());
		
		if(valid.getErrors().size() > 0) {
			throw valid;
		}
		return obj;
	}

	@FXML
	public void onbtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeNodes();

	}

	private void initializeNodes() {
		Constraints.setTextFieldDouble(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);

	}

	public void createUpdateData() {
		if (entity == null) {
			throw new IllegalStateException("Entity is null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}

	private void setErrorsMessage(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if(fields.contains("name")) {
			labelErroName.setText(errors.get("name"));
		}
	}
}
