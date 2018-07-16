import java.util.ArrayList;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AdditionScreen  extends VBox{
		PageLabel additionTitle;
		AdditionPane iDPane;
		AdditionLabel iDLabel;
		AdditionPane genericPane;
		AdditionLabel genericLabel; 
		InventoryTextFields genericFill;
		AdditionLabel genericSideLabel;
		HBox additionButtonPane;
		InventoryButton addCompoundButton;
		InventoryTextFields iDFill;
		AdditionLabel ec50Label;
		InventoryTextFields ec50TextField;
		AdditionLabel ec90Label;
		InventoryTextFields ec90TextField;
		AdditionLabel purityLabel;
		InventoryTextFields purityField;
		AdditionLabel sourceLabel;
		InventoryTextFields sourceField;
		AdditionLabel sourceInstructionLabel;
		TextArea sourceInstructionField;
		ArrayList<Compound> addedCompounds;
		AdditionLabel aliquotConcentrationLabel;
		InventoryTextFields aliquotConcentration;
		AdditionLabel numberOfAliquotsLabel;
		ComboBox<Integer> numberOfAliquots; 
		AdditionLabel aliquotVolumeLabel;
		InventoryTextFields aliquotVolume;

		AdditionScreen(){
			
			//Initializing the Nodes 
			this.setAlignment(Pos.CENTER);this.setSpacing(50);
			this.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, null, null)));
			additionTitle = new PageLabel("Adding Compounds");
			iDPane = new AdditionPane();
			iDLabel = new AdditionLabel("Compound ID:"); iDFill= new InventoryTextFields();
			genericPane = new AdditionPane();
			genericLabel = new AdditionLabel("Generic Name:");genericFill = new InventoryTextFields();
			genericSideLabel= new AdditionLabel("(optional)");
			ec90Label = new AdditionLabel("ec90 Concentration:"); ec90TextField = new InventoryTextFields(); 
			ec50Label = new AdditionLabel("ec50 Concentration:"); ec50TextField = new InventoryTextFields();
			purityLabel = new AdditionLabel("Purtiy"); purityField = new InventoryTextFields();
			sourceLabel = new AdditionLabel("Compound Source:"); sourceField = new InventoryTextFields();
			aliquotConcentrationLabel = new AdditionLabel("Aliquot Concentration:"); aliquotConcentration = new InventoryTextFields();
			aliquotVolumeLabel = new AdditionLabel("Aliquot Volume (uL):"); aliquotVolume = new InventoryTextFields();
			numberOfAliquotsLabel = new AdditionLabel("Number of Aliquots"); numberOfAliquots = new ComboBox<>();
			for (int i = 1; i < 21; i++){
				numberOfAliquots.getItems().add(i);
			}
			numberOfAliquots.setStyle("-fx-color:white"); numberOfAliquots.setValue(1); numberOfAliquots.setPrefSize(100, 40); 
			sourceInstructionLabel = new AdditionLabel("Instructions for obtaining additional Ccompounds:");
			sourceInstructionField = new TextArea(); sourceInstructionField.setPrefColumnCount(20);
			sourceInstructionField.setPrefRowCount(5); sourceInstructionField.setWrapText(true);
			sourceInstructionField.setStyle("-fx-text-fill: black"); sourceInstructionField.setFont(Font.font("Arial", 20));
			
			
			
			//Adding to the Pane
			genericPane.add(iDLabel, 0,0);genericPane.add(iDFill, 1, 0);genericPane.add(genericLabel, 0, 1);genericPane.add(genericFill, 1, 1);
			genericPane.add(aliquotConcentrationLabel, 2, 0); genericPane.add(aliquotConcentration, 3, 0);
			genericPane.add(aliquotVolumeLabel, 2, 1);genericPane.add(aliquotVolume, 3, 1);
			genericPane.add(numberOfAliquotsLabel, 2, 2); genericPane.add(numberOfAliquots, 3, 2);
			genericPane.add(ec90Label, 0, 2);genericPane.add(ec90TextField, 1, 2);
			genericPane.add(ec50Label, 0, 3); genericPane.add(ec50TextField, 1, 3); genericPane.add(purityLabel, 0, 4);genericPane.add(purityField, 1, 4);
			genericPane.add(sourceLabel, 0, 5); genericPane.add(sourceField, 1, 5);genericPane.add(sourceInstructionLabel, 0, 6); genericPane.add(sourceInstructionField, 2, 6);
			genericPane.setVgap(25);
			genericPane.setHgap(25);
			additionButtonPane = new HBox(); additionButtonPane.setSpacing(20); additionButtonPane.setAlignment(Pos.CENTER);
			addCompoundButton = new InventoryButton("Add Compound");
			additionButtonPane.getChildren().addAll(addCompoundButton);
			this.getChildren().addAll(additionTitle, iDPane, genericPane, additionButtonPane);
			this.addedCompounds = new ArrayList<Compound>();
			
			//Setting action events
			addCompoundButton.setOnAction(e->{
				if(!this.missingRequiredEntries()){
					this.addedCompounds.add(new Compound(
							this.getGenericFill().getText(),
							this.getiDFill().getText(), 
							Double.parseDouble(this.getValueEntry(this.getEc90TextField().getText())),
							Double.parseDouble(this.getValueEntry(this.getEc50TextField().getText())), 
							Double.parseDouble(this.getValueEntry(this.getPurityField().getText())), 
							this.getSourceField().getText(), 
							this.getSourceInstructionField().getText(), 
							Double.parseDouble(this.getValueEntry(this.getAliquotConcentration().getText())), 
							Double.parseDouble(this.getValueEntry(this.getAliquotVolume().getText())), 
							this.numberOfAliquots.getValue()
							));
				
				}	
			});
			
		}
		
		public String getValueEntry(String entry){
			if (entry.isEmpty()){
				entry =  "0";
			}
			return entry;
		}
		
		/**
		 * This method looks for missing feilds in the "Add Compound" Screen. Currently, If compound ID, purity, aliquot volume, aliquot
		 * concentration, or the compound source is missing, then the Labels are set to turn red, and it stops the compound from being
		 * entered. Returns false true if any of the vital feilds are missing. 
		 * @return true is there is a required field missing and false otherwise. 
		 */
		public boolean missingRequiredEntries(){
			boolean value = false;
			if(this.getiDFill().getText().isEmpty()){
				value = true;
				this.iDLabel.setId("flagged_entries");
			}
			if(this.getPurityField().getText().isEmpty()){
				value = true;
				this.purityLabel.setId("flagged_entries");
			}
			if(this.getSourceField().getText().isEmpty()){
				value = true;
				this.sourceLabel.setId("flagged_entries");
			}
			if(this.getAliquotVolume().getText().isEmpty()){
				value = true;
				this.aliquotVolumeLabel.setId("flagged_entries");
			}
			if(this.getAliquotConcentration().getText().isEmpty()){
				value = true;
				this.aliquotConcentrationLabel.setId("flagged_entries");
			}
			if(value == false){
				this.aliquotConcentrationLabel.setId(null);
				this.aliquotVolumeLabel.setId(null);
				this.sourceLabel.setId(null);
				this.iDLabel.setId(null);
				this.purityLabel.setId(null);
			}
			return value;
		}
		

		/**
		 * This is a method called in the Inventory GUI controller method, it allows the use of a simgle "Back" button, which gets thrown into the current active screen
		 * @param backButton there is only one in the entire application
		 */
		void buildScreen(InventoryButton backButton){
			if(this.additionButtonPane.getChildren().size() > 1){
				this.additionButtonPane.getChildren().remove(backButton);
			}
			this.additionButtonPane.getChildren().add(backButton);
		}
		
		/**
		 * @return the aliquotConcentration
		 */
		public InventoryTextFields getAliquotConcentration() {
			return aliquotConcentration;
		}

		/**
		 * @return the numberOfAliquots
		 */
		public ComboBox<Integer> getNumberOfAliquots() {
			return numberOfAliquots;
		}

		/**
		 * @return the aliquotVolume
		 */
		public InventoryTextFields getAliquotVolume() {
			return aliquotVolume;
		}

		/**
		 * @param aliquotConcentration the aliquotConcentration to set
		 */
		public void setAliquotConcentration(InventoryTextFields aliquotConcentration) {
			this.aliquotConcentration = aliquotConcentration;
		}

		/**
		 * @param numberOfAliquots the numberOfAliquots to set
		 */
		public void setNumberOfAliquots(ComboBox<Integer> numberOfAliquots) {
			this.numberOfAliquots = numberOfAliquots;
		}

		/**
		 * @param aliquotVolume the aliquotVolume to set
		 */
		public void setAliquotVolume(InventoryTextFields aliquotVolume) {
			this.aliquotVolume = aliquotVolume;
		}
		
		/**
		 * @return the genericFill
		 */
		public InventoryTextFields getGenericFill() {
			return genericFill;
		}

		/**
		 * @return the iDFill
		 */
		public InventoryTextFields getiDFill() {
			return iDFill;
		}

		/**
		 * @return the ec50TextField
		 */
		public InventoryTextFields getEc50TextField() {
			return ec50TextField;
		}

		/**
		 * @return the purityField
		 */
		public InventoryTextFields getPurityField() {
			return purityField;
		}

		/**
		 * @return the sourceField
		 */
		public InventoryTextFields getSourceField() {
			return sourceField;
		}

		/**
		 * @return the sourceInstructionField
		 */
		public TextArea getSourceInstructionField() {
			return sourceInstructionField;
		}

		/**
		 * @param genericFill the genericFill to set
		 */
		public void setGenericFill(InventoryTextFields genericFill) {
			this.genericFill = genericFill;
		}

		/**
		 * @param iDFill the iDFill to set
		 */
		public void setiDFill(InventoryTextFields iDFill) {
			this.iDFill = iDFill;
		}

		/**
		 * @param ec50TextField the ec50TextField to set
		 */
		public void setEc50TextField(InventoryTextFields ec50TextField) {
			this.ec50TextField = ec50TextField;
		}

		/**
		 * @param purityField the purityField to set
		 */
		public void setPurityField(InventoryTextFields purityField) {
			this.purityField = purityField;
		}

		/**
		 * @param sourceField the sourceField to set
		 */
		public void setSourceField(InventoryTextFields sourceField) {
			this.sourceField = sourceField;
		}

		/**
		 * @param sourceInstructionField the sourceInstructionField to set
		 */
		public void setSourceInstructionField(TextArea sourceInstructionField) {
			this.sourceInstructionField = sourceInstructionField;
		}

		/**
		 * @param addedCompounds the addedCompounds to set
		 */
		public void setAddedCompounds(ArrayList<Compound> addedCompounds) {
			this.addedCompounds = addedCompounds;
		}


		/**
		 * @return the addedCompounds
		 */
		public ArrayList<Compound> getAddedCompounds() {
			return addedCompounds;
		}


		/**
		 * @return the ec90TextField
		 */
		public InventoryTextFields getEc90TextField() {
			return ec90TextField;
		}


		/**
		 * @param ec90TextField the ec90TextField to set
		 */
		public void setEc90TextField(InventoryTextFields ec90TextField) {
			this.ec90TextField = ec90TextField;
		}


//		class AddCompoundHandler implements EventHandler<ActionEvent>{
//			@Override
//			public void handle(ActionEvent e){
//				
//			}
			
		//}
		
		
}

