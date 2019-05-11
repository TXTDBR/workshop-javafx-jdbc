package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
		} catch (DbException e) {
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
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		obj.setName(txtName.getText());
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

}
