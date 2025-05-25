package com.example.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextField;


public class HelloController {


	@FXML
	private Label timerLabel;

	@FXML
	private TextField focusInput;

	@FXML
	private Label statusLabel;

	@FXML
	private TextField taskInput;

	@FXML
	private ListView<String> taskList;






	private Timeline timeline;
	private int remainingSeconds;




	private ObservableList<String> tasks;

	@FXML
	public void initialize() {
		remainingSeconds = 25 * 60; // default 25 minutes
		updateTimerLabel();
		tasks = FXCollections.observableArrayList();
		taskList.setItems(tasks);

		taskList.setCellFactory(lv -> new ListCell<String>() {
			private final HBox container = new HBox(10);
			private final Text taskText = new Text();
			private final Button deleteButton = new Button("Delete");

			{
				deleteButton.getStyleClass().add("delete-button");
				deleteButton.setOnAction(event -> {
					getListView().getItems().remove(getItem());
				});
				container.getChildren().addAll(taskText, deleteButton);
				HBox.setHgrow(taskText, Priority.ALWAYS);
			}

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty || item == null) {
					setText(null);
					setGraphic(null);
				} else {
					taskText.setText(item);
					setGraphic(container);
				}
			}
		});
	}
	@FXML
	private void addTask() {
		String task = taskInput.getText();
		if (task != null && !task.trim().isEmpty()) {
			taskList.getItems().add(task.trim());
			taskInput.clear();
		}
	}

	@FXML
	private void onSaveButtonClick() {
		String input = focusInput.getText();
		try {
			int minutes = Integer.parseInt(input);
			if (minutes <= 0) {
				statusLabel.setText("Enter a positive number");
				return;
			}
			remainingSeconds = minutes * 60;
			updateTimerLabel();
			statusLabel.setText("Focus time set to " + minutes + " minutes.");
		} catch (NumberFormatException e) {
			statusLabel.setText("Invalid number");
		}
	}

	@FXML
	private void startTimer() {
		if (timeline != null && timeline.getStatus() == Timeline.Status.RUNNING) {
			return; // already running
		}
		statusLabel.setText("Timer started");
		timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
			if (remainingSeconds > 0) {
				remainingSeconds--;
				updateTimerLabel();
			} else {
				timeline.stop();
				statusLabel.setText("Time's up!");
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}

	@FXML
	private void pauseTimer() {
		if (timeline != null) {
			timeline.pause();
			statusLabel.setText("Timer paused");
		}
	}



	@FXML
	private void resetTimer() {
		if (timeline != null) {
			timeline.stop();
		}
		remainingSeconds = 25 * 60; // reset to default or keep as is
		updateTimerLabel();
		statusLabel.setText("Timer reset");
	}

	private void updateTimerLabel() {
		int minutes = remainingSeconds / 60;
		int seconds = remainingSeconds % 60;
		timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
	}
}
