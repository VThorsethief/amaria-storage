import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;






public class InventoryGUI extends Application implements InventoryWorkbookReader{
	ScreenController controller = new ScreenController();
	private enum modes {START, SEARCH, ADD, MENU, COMPUTE};
	private modes currentModes = modes.START;
	InventoryButton backButton;
	AdditionScreen additionScreen;
	SearchScreen searchScreen;
	ComputationScreen compScreen;
	ArrayList<Freezerbox> loadedBoxes;
	ArrayList<Compound> loadedCompounds;
	Freezerbox sampleBox;
	Compound sampleCompound;
	final static double SCREEN_AREA = Screen.getPrimary().getBounds().getHeight() * Screen.getPrimary().getBounds().getWidth();

	
	@Override
	public void start(Stage primaryStage){
		
		//Priming GUI
		primaryStage.setHeight(Screen.getPrimary().getBounds().getHeight() *.8); 
		primaryStage.setWidth(Screen.getPrimary().getBounds().getWidth() *.8); //primaryStage.setFullScreen(true);
		VBox titlePane = new VBox(); titlePane.setAlignment(Pos.CENTER); 
		PageLabel title = new PageLabel("Welcome to Amaria");
		title.setId("PageTitles");
		title.getStyleClass().add("PageTitles");
		//Title
		
		title.setFont(Font.font("Arial",  FontWeight.EXTRA_BOLD, primaryStage.getWidth()*.07));title.setTextFill(Color.WHITE); 
		title.setPadding(new Insets(primaryStage.getHeight()*.01,
				primaryStage.getHeight()*.01,primaryStage.getHeight()*.2,primaryStage.getHeight()*.01));
		
		
		//Start Button 
		InventoryButton startButton = new InventoryButton("Begin"); 
		startButton.setPrefSize(primaryStage.getWidth()*.2, primaryStage.getHeight()*.2);
		backButton = new InventoryButton("Back");
		
		//Data entry
		MenuScreen menuPane = new MenuScreen();
		controller.addScreen("menu", menuPane);
		
		sampleBox = new Freezerbox();
		loadedBoxes = new ArrayList<Freezerbox>();
		loadedBoxes.add(sampleBox);
		loadCompounds();
		
		//Search Pane
		searchScreen = new SearchScreen(this.getLoadedCompounds());
		controller.addScreen("search", searchScreen);
		controller.addScreen("Results", searchScreen.getResultScreen());
		
		
		
		//Add Compounds Screen
		additionScreen = new AdditionScreen();
		controller.addScreen("add", additionScreen);
		
		//Add Computation Screen
		compScreen = new ComputationScreen(this.loadedCompounds);
		controller.addScreen("compute", compScreen);
		

		//Setting GUI
		titlePane.getChildren().addAll(title, startButton);
		Scene scene = new Scene(titlePane, primaryStage.getWidth()* .4,primaryStage.getHeight()* .5);
		controller.initializeController(scene);
		controller.addScreen("title", titlePane);
		
		//Action Events 
		ButtonHandler screenTransition = new ButtonHandler();
		startButton.setOnAction(screenTransition);
		menuPane.getSearchButton().setOnAction(screenTransition);
		menuPane.getAddButton().setOnAction(screenTransition);
		menuPane.getComputeButton().setOnAction(screenTransition);
		searchScreen.getSearchSearchButton().setOnAction(screenTransition);
		backButton.setOnAction(screenTransition);
		
		
		primaryStage.setTitle("Amaria Start Screen");
		primaryStage.setScene(scene);
		scene.getStylesheets().add("InventoryStyleSheet.css");
		primaryStage.show();
		
	}
		
	class ButtonHandler implements EventHandler<ActionEvent>{
			@Override
			public void handle(ActionEvent e){
				switch(((Button) e.getSource()).getText()){
				case "Begin": controller.activate("menu");setScreenMode(modes.MENU);break;
				case "Search": controller.activate("search");
						searchScreen.buildScreen(backButton, loadedCompounds);
						setScreenMode(modes.SEARCH);break;
				case "Add": controller.activate("add"); 
						additionScreen.buildScreen(backButton); 
						setScreenMode(modes.ADD); break;
				case "Compute": controller.activate("compute");
						compScreen.buildScreen(backButton);
						setScreenMode(modes.COMPUTE); break;
				case "Back":{
					switch(getScreenMode()){
					case SEARCH: controller.activate("menu"); break;
					case ADD: controller.activate("menu"); saveCompounds();break; 
					case START: controller.activate("start");break;
					case MENU: controller.activate("start");break;
					case COMPUTE: controller.activate("menu"); break;
					}
				}; break;
				case "Search Compounds": searchScreen.resultScreen.buildScreen(backButton); 
				controller.activate("Results"); break;
				case "Remove":  
					Compound selectedItem = searchScreen.resultScreen.getMatch(
							searchScreen.resultScreen.getAvailableCompounds().getSelectionModel().getSelectedItem());
					if(selectedItem.getNumberOfaAliquots() < 2){
						searchScreen.removeCompound(selectedItem);
						searchScreen.getResultScreen().removeCompound(selectedItem);
					}else{
						selectedItem.removeAliquots(1);
					};break;
				};
				
					
			}
	}
	
    private void setScreenMode(modes newMode){
    	this.currentModes = newMode;
    }
    
    private modes getScreenMode(){
    	return this.currentModes;
    }
    
	public static void main(String[] args){			//launch, not needed for executing from the command line 
		Application.launch(args);
	}

	/**
	 * @return the additionScreen
	 */
	public AdditionScreen getAdditionScreen() {
		return additionScreen;
	}

	/**
	 * @param additionScreen the additionScreen to set
	 */
	public void setAdditionScreen(AdditionScreen additionScreen) {
		this.additionScreen = additionScreen;
	}
	


	/**
	 * @return the loadedBoxes
	 */
	public ArrayList<Freezerbox> getLoadedBoxes() {
		return loadedBoxes;
	}

	/**
	 * @return the loadedCompounds
	 */
	public ArrayList<Compound> getLoadedCompounds() {
		return this.loadedCompounds;
	}

	/**
	 * @param loadedBoxes the loadedBoxes to set
	 */
	public void setLoadedBoxes(ArrayList<Freezerbox> loadedBoxes) {
		this.loadedBoxes = loadedBoxes;
	}

	/**
	 * @param loadedCompounds the loadedCompounds to set
	 */
	public void setLoadedCompounds(ArrayList<Compound> loadedCompounds) {
		this.loadedCompounds = loadedCompounds;
	}
	
	public void setEmptyCompound(Compound emptyCompound){
		this.loadedCompounds.clear();
		this.loadedCompounds.add(emptyCompound);
	}

	/**
	 * Saves the compounds in the addition screen to the loaded compounds in the main data field. Adds the Box Number to the compound.
	 * There is a possibility that this could change the bax number everytime the box gets saved. 
	 * The is Full function is finicky, if there's problems in the future with box numbering, it might be here
	 */
	public void saveCompounds(){
		
		for (int i = 0; i < this.getAdditionScreen().getAddedCompounds().size(); i++){
			int boxIndex = 0;
			while(this.getLoadedBoxes().get(boxIndex).isFull()){
				boxIndex++;
			}
			this.getAdditionScreen().getAddedCompounds().get(i).setBoxNumber(boxIndex);
			this.getLoadedBoxes().get(boxIndex).addCompound(this.getAdditionScreen().getAddedCompounds().get(i));			
			
		}
		this.getLoadedCompounds().addAll(this.getAdditionScreen().getAddedCompounds());
//		for (int k = 0; k< this.getLoadedCompounds().size(); k++){
//			System.out.println(k);
//			System.out.println(this.getLoadedCompounds().get(k).toString());
//		}
		
		try{
			InventoryWorkbookReader.writeInventory(this.getLoadedCompounds());
		}catch(IOException ex){
			System.out.println("ERROR Writing Compounds");
		}
	}
	
	public void loadCompounds(){
		try{
			this.setLoadedCompounds(InventoryWorkbookReader.readInventory());
		}catch(IOException exR){
			System.out.println("Error reading File");
			
		}catch(InvalidFormatException exR2){
			System.out.println("InValid Format");
		}catch(EncryptedDocumentException exR3){
			//This needs to change to a pop out window
			System.out.println("You do not have access to thei file.");
		}catch(EmptyWbkException exR4){
			this.setEmptyCompound(new Compound("EMPTY WORKBOOK", "EMPTY WORKBOOK"));
		}
	}
}

class SearchScreen extends VBox{
	PageLabel searchTitle;
	HBox searchOptions;
	RadioButton genericOption;
	RadioButton compIDOption;
	ToggleGroup searchOptionsToggle;
	TextField searchBar;
	InventoryButton searchSearchButton;
	HBox searchButtonPane;
	ArrayList<Compound> compounds;
	ResultScreen resultScreen;
	
	
	SearchScreen(ArrayList<Compound> importedCompounds){
		this.setAlignment(Pos.CENTER);this.setSpacing(150);
		this.setBackground(new Background(new BackgroundFill(Color.CORNFLOWERBLUE, null, null)));
		searchTitle = new PageLabel("Search for Compounds");
		searchOptions = new HBox(); searchOptions.setAlignment(Pos.CENTER); searchOptions.setSpacing(25);
		genericOption = new RadioButton("Search by Generic Name"); genericOption.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		compIDOption = new RadioButton("Search by Compound ID");
		compIDOption.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		searchOptionsToggle = new ToggleGroup();
		genericOption.setToggleGroup(searchOptionsToggle);compIDOption.setToggleGroup(searchOptionsToggle);
		searchBar = new TextField();
		searchBar.setStyle("-fz-text-fill: black");
		searchBar.setFont(Font.font("Times", 20));
		searchBar.setAlignment(Pos.CENTER);
		searchBar.setMaxWidth(500); //This needs to change so it scales with screen size
		searchOptions.getChildren().addAll(genericOption, compIDOption);
		searchSearchButton = new InventoryButton("Search Compounds");
		searchButtonPane = new HBox(); searchButtonPane.setAlignment(Pos.CENTER);searchButtonPane.setSpacing(20);
		searchButtonPane.getChildren().addAll(searchSearchButton);
		this.getChildren().addAll(searchTitle, searchOptions, searchBar, searchButtonPane);
		this.setCompounds(importedCompounds);
		
		resultScreen = new ResultScreen(this.getCompounds());
	}
	
	void buildScreen(InventoryButton backButton, ArrayList<Compound> loadedCompounds){
		if(this.searchButtonPane.getChildren().size() > 1){
			this.searchButtonPane.getChildren().remove(backButton);
		}
		this.searchButtonPane.getChildren().add(backButton);
		this.compounds = loadedCompounds;
	}
	
	/**
	 * @return the compounds
	 */
	public ArrayList<Compound> getCompounds() {
		return compounds;
	}

	/**
	 * @param compounds the compounds to set
	 */
	public void setCompounds(ArrayList<Compound> compounds) {
		this.compounds = compounds;
	}
	
	/**
	 * @return the resultScreen
	 */
	public ResultScreen getResultScreen() {
		return resultScreen;
	}

	/**
	 * @param resultScreen the resultScreen to set
	 */
	public void setResultScreen(ResultScreen resultScreen) {
		this.resultScreen = resultScreen;
	}

	/**
	 * @return the searchSearchButton
	 */
	public InventoryButton getSearchSearchButton() {
		return searchSearchButton;
	}

	/**
	 * @param searchSearchButton the searchSearchButton to set
	 */
	public void setSearchSearchButton(InventoryButton searchSearchButton) {
		this.searchSearchButton = searchSearchButton;
	}
	
	public boolean removeCompound(Compound targetCompound){
		return this.getCompounds().remove(targetCompound);
		
	}

	class ResultScreen extends HBox{
		PageLabel searchResultsTitle;
		ResultGrid compoundPane; 
		VBox buttonPane;
		ResultButton editButton;
		ResultButton removeButton;
		ResultButton computeButton;
		ResultButton newSearchButton;
		ComboBox<String> availableCompounds;
		ArrayList<Compound> compounds;
		ResultButton workingStockButton;
		ResultButton exitCalcButton;

		ResultScreen(ArrayList<Compound> importedCompounds){
			this.setAlignment(Pos.CENTER_LEFT);this.setSpacing(Screen.getPrimary().getBounds().getWidth() *.02);
			compoundPane = new ResultGrid(); //compoundPane.setId("CompoundPane");
			buttonPane = new VBox(); buttonPane.setAlignment(Pos.CENTER_LEFT); buttonPane.setSpacing(30);
			editButton = new ResultButton("Edit"); removeButton = new ResultButton("Remove"); 
			computeButton = new ResultButton("Compute"); newSearchButton = new ResultButton("Search \nNew Compound");
			availableCompounds = new ComboBox<>(); availableCompounds.setId("CompoundMenu");
			this.compounds = importedCompounds;
			availableCompounds.getItems().clear();
			for (int c = 0; c < this.getCompounds().size(); c++){
				availableCompounds.getItems().add(this.getCompounds().get(c).toString());
			}
			availableCompounds.setPrefSize(Screen.getPrimary().getBounds().getWidth() *.15, Screen.getPrimary().getBounds().getHeight() *.08);
			availableCompounds.setValue("Choose Compound");
			
			// The Caculation buttons 
			workingStockButton = new ResultButton("Make Working Stock");
			exitCalcButton = new ResultButton("Exit Calculation");
			
			
			//Lambda Event Handlers 
			availableCompounds.setOnAction(e->{
				compoundPane.setActive(this.getMatch(availableCompounds.getSelectionModel().getSelectedItem()));
			});
			removeButton.setOnAction(e->{
				this.removeCompound(this.getMatch(availableCompounds.getSelectionModel().getSelectedItem()));
			});
			computeButton.setOnAction(e->{
				buttonPane.getChildren().clear();
				buttonPane.getChildren().addAll(workingStockButton, exitCalcButton);
			});
			
			exitCalcButton.setOnAction(e->{
				buttonPane.getChildren().clear();
				buttonPane.getChildren().addAll(availableCompounds, editButton, removeButton, computeButton, newSearchButton);
			});
			
			workingStockButton.setOnAction(e->{
				this.compoundPane.getChildren().clear();
				
			});
			
			
			buttonPane.getChildren().addAll(availableCompounds, editButton, removeButton, computeButton);
			this.getChildren().addAll(compoundPane, buttonPane);
			
		
			
			
			
		}
		
		public void changeButtonFormat(){
			this.buttonPane.getChildren().clear();
			
		}
		
		/**
		 * @return the compounds
		 */
		public ArrayList<Compound> getCompounds() {
			return compounds;
		}
		/**
		 * @param compounds the compounds to set
		 */
		public void setCompounds(ArrayList<Compound> compounds) {
			this.compounds = compounds;
		}
		/**
		 * @return the availableCompounds
		 */
		public ComboBox<String> getAvailableCompounds() {
			return availableCompounds;
		}
		/**
		 * @param availableCompounds the availableCompounds to set
		 */
		public void setAvailableCompounds(ComboBox<String> availableCompounds) {
			this.availableCompounds = availableCompounds;
		}
		void buildScreen(InventoryButton backButton){
			if(this.buttonPane.getChildren().size() > 5){
				this.buttonPane.getChildren().remove(backButton);
			}
			this.buttonPane.getChildren().add(backButton);
			availableCompounds.getItems().clear();
			for (int c = 0; c < this.getCompounds().size(); c++){
				if(c == 0 || !this.getCompounds().get(c).equals(this.getCompounds().get(c - 1))){
					availableCompounds.getItems().add(this.getCompounds().get(c).toString());
				}
			}
		}
		public Compound getMatch(String genericID){
			int i = 0; 
			if(genericID != null){
				for (int d = 0; d < this.compounds.size(); d++){
				 if(this.compounds.get(d).getCompoundID().compareTo(genericID) == 0){
					 i = d;
					 break;
				 }
			 }
				return (Compound)this.compounds.get(i);
			}else{
				return null;
			}
			
		 }
		
		public boolean removeCompound(Compound targetCompound){
			compoundPane.clearQuery();
			this.getAvailableCompounds().setValue("No compounds listed");
			if(this.getCompounds().remove(targetCompound)){
				this.availableCompounds.getItems().clear();
				for (int c = 0; c < this.getCompounds().size(); c++){
					availableCompounds.getItems().add(this.getCompounds().get(c).toString());
				}
				System.out.println("Remove ResultScreen Button pressed");
				return true;
			}else{
				return false;
			}
			
		}
		
		class ResultGrid extends GridPane{
			ResultLabel genericLabel;
			ResultLabel compoundIDLabel;
			ResultLabel ec50Label;
			ResultLabel ec90Label;
			ResultLabel volumeLabel;
			ResultLabel concentrationLabel;
			ResultLabel aliquotNumLabel;
			
			//The Results from the selected compound
			QueryLabel queryGeneric;
			QueryLabel queryID;
			QueryLabel queryEc50;
			QueryLabel queryEc90;
			QueryLabel queryVolume;
			QueryLabel queryConcentration;
			QueryLabel queryAliquotNum;
			
			
			
			
			ResultGrid(){
				this.setAlignment(Pos.CENTER_LEFT);
				this.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
				this.setLayoutX(Screen.getPrimary().getBounds().getWidth() *.6);
				this.setLayoutY(Screen.getPrimary().getBounds().getHeight());
				
				
				genericLabel = new ResultLabel("Generic Name: ");
				compoundIDLabel = new ResultLabel("Compound ID: ");
				ec50Label = new ResultLabel("EC50: ");
				ec90Label = new ResultLabel("EC90: ");
				volumeLabel = new ResultLabel("Aliquot Volume: ");
				concentrationLabel = new ResultLabel("Aliquot Concentration: ");
				aliquotNumLabel = new ResultLabel("Number of Aliquots: ");
				
				queryGeneric = new QueryLabel(""); queryID = new QueryLabel("");
				queryEc50 = new QueryLabel(""); queryEc90 = new QueryLabel(""); 
				queryVolume = new QueryLabel(""); queryConcentration = new QueryLabel("");
				queryAliquotNum = new QueryLabel("");
				
				
				
				this.add(genericLabel, 0, 0); this.add(compoundIDLabel, 0, 1);
				this.add(ec50Label, 0, 2); this.add(ec90Label, 0, 3); this.add(volumeLabel, 0, 4);
				this.add(concentrationLabel, 0, 5); this.add(aliquotNumLabel, 0, 6);
				
				this.add(queryGeneric, 1, 0); this.add(queryID, 1, 1); this.add(queryEc50, 1, 2);  //continue here, adding results in 
				this.add(queryEc90, 1, 3); this.add(queryVolume, 1, 4); this.add(queryConcentration, 1, 5);
				this.add(queryAliquotNum, 1, 6);
				
				
			}
			
			public void clearQuery(){
				for(Node x : this.getChildren()){
					if(x instanceof QueryLabel){
						((QueryLabel) x).setText("");
					}
				}
			}
			
			public void setActive(Compound active){
				if(active != null){
					this.setQueryID(active.getCompoundID());
					this.setQueryGeneric(active.getGenericName());
					this.setQueryVolume(active.getVolume() + "");
					this.setQueryEc50(active.getEc50() + "");
					this.setQueryEc90(active.getEc90() + "");
					this.setQueryConcentration(active.getConcentration() + "");
					this.setQueryAliquotNum(active.getNumberOfaAliquots() + "");
				}else{
					this.clearQuery();
				}
				
			}
			
			



			/**
			 * @return the genericLabel
			 */
			public ResultLabel getGenericLabel() {
				return genericLabel;
			}



			/**
			 * @return the queryGeneric
			 */
			public ResultLabel getQueryGeneric() {
				return queryGeneric;
			}



			/**
			 * @return the queryID
			 */
			public ResultLabel getQueryID() {
				return queryID;
			}



			/**
			 * @return the queryEc50
			 */
			public ResultLabel getQueryEc50() {
				return queryEc50;
			}



			/**
			 * @return the queryEc90
			 */
			public ResultLabel getQueryEc90() {
				return queryEc90;
			}



			/**
			 * @return the queryVolume
			 */
			public ResultLabel getQueryVolume() {
				return queryVolume;
			}




			/**
			 * @param queryGeneric the queryGeneric to set
			 */
			public void setQueryGeneric(String queryGeneric) {
				this.queryGeneric.setText(queryGeneric);;
			}



			/**
			 * @param queryID the queryID to set
			 */
			public void setQueryID(String queryID) {
				this.queryID.setText(queryID);
			}



			/**
			 * @param queryEc50 the queryEc50 to set
			 */
			public void setQueryEc50(String queryEc50) {
				this.queryEc50.setText(queryEc50);
			}



			/**
			 * @param queryEc90 the queryEc90 to set
			 */
			public void setQueryEc90(String queryEc90) {
				this.queryEc90.setText(queryEc90);
			}



			/**
			 * @param queryVolume the queryVolume to set
			 */
			public void setQueryVolume(String queryVolume) {
				this.queryVolume.setText(queryVolume);
			}





			/**
			 * @return the queryConcentration
			 */
			public QueryLabel getQueryConcentration() {
				return queryConcentration;
			}





			/**
			 * @return the queryAliquotNum
			 */
			public QueryLabel getQueryAliquotNum() {
				return queryAliquotNum;
			}





			/**
			 * @param queryConcentration the queryConcentration to set
			 */
			public void setQueryConcentration(String queryConcentration) {
				this.queryConcentration.setText(queryConcentration);
			}





			/**
			 * @param queryAliquotNum the queryAliquotNum to set
			 */
			public void setQueryAliquotNum(String queryAliquotNum) {
				this.queryAliquotNum.setText(queryAliquotNum);
			}
		}
		class ResultLabel extends AdditionLabel{
			ResultLabel(String label){
				super(label);
				this.setFont(Font.font((Screen.getPrimary().getBounds().getHeight() * 
						Screen.getPrimary().getBounds().getWidth()) *.00002));
				this.setAlignment(Pos.CENTER_RIGHT);
				this.setPrefSize(Screen.getPrimary().getBounds().getWidth() *.25, 
						Screen.getPrimary().getBounds().getHeight() *.01);
//				this.setMinSize(Screen.getPrimary().getBounds().getWidth() *.1, 
//						Screen.getPrimary().getBounds().getHeight() *.01);
				this.setId("queryLabels");
				
			}
		}
		class QueryLabel extends ResultLabel{
			QueryLabel(String label){
				super(label);
				this.setId("query_font");
			}
		}
	}
}

class MenuScreen extends GridPane{
		GridPane menuPane;
		ArrayList<InventoryButton> menuButtons;
		InventoryButton searchButton;
		InventoryButton addButton;
		InventoryButton removeButton;
		InventoryButton computeButton;
		
		MenuScreen(){
			this.setAlignment(Pos.CENTER);
			menuButtons = new ArrayList<InventoryButton>();
			searchButton = new InventoryButton("Search");
			menuButtons.add(searchButton);
			addButton = new InventoryButton("Add");
			menuButtons.add(addButton);
			removeButton = new InventoryButton("Remove");
			menuButtons.add(removeButton);
			computeButton = new InventoryButton("Compute");
			menuButtons.add(computeButton);
			
			
			this.add(searchButton,0,0);
			this.add(addButton,2,0);
			this.add(removeButton,0,2);
			this.add(computeButton,2,2);
			this.setHgap(50);
			this.setVgap(50);
			
			for(InventoryButton x : menuButtons){
				x.setPadding(new Insets(100));
				GridPane.setHalignment(x, HPos.CENTER);
			}
		}

		/**
		 * @return the searchButton
		 */
		public InventoryButton getSearchButton() {
			return searchButton;
		}

		/**
		 * @return the addButton
		 */
		public InventoryButton getAddButton() {
			return addButton;
		}

		/**
		 * @return the removeButton
		 */
		public InventoryButton getRemoveButton() {
			return removeButton;
		}

		/**
		 * @return the computeButton
		 */
		public InventoryButton getComputeButton() {
			return computeButton;
		}

		/**
		 * @param searchButton the searchButton to set
		 */
		public void setSearchButton(InventoryButton searchButton) {
			this.searchButton = searchButton;
		}

		/**
		 * @param addButton the addButton to set
		 */
		public void setAddButton(InventoryButton addButton) {
			this.addButton = addButton;
		}

		/**
		 * @param removeButton the removeButton to set
		 */
		public void setRemoveButton(InventoryButton removeButton) {
			this.removeButton = removeButton;
		}

		/**
		 * @param computeButton the computeButton to set
		 */
		public void setComputeButton(InventoryButton computeButton) {
			this.computeButton = computeButton;
		}
	}

class ScreenController {
    private HashMap<String, Pane> screenMap = new HashMap<>();
    private Scene main;

    public ScreenController() {
    }
    
    
    public ScreenController(Scene main) {
        this.main = main;
    }
    
    public void initializeController(Scene main){
    	this.main = main;
    }

    protected void addScreen(String name, Pane pane){
         screenMap.put(name, pane);
    }

    protected void removeScreen(String name){
        screenMap.remove(name);
    }

    protected void activate(String name){
        main.setRoot(screenMap.get(name) );
    }
    
    protected Parent getActive(){
    	return this.main.getRoot();
    }
    
}

class InventoryButton extends Button{
	public InventoryButton(String label){
		this.setText(label);
		this.setFont(Font.font("Arial",  FontWeight.BOLD, 
				30));
		this.setPadding(new Insets(20));
	}
	
	@Override
	public String toString(){
		return this.getText();
	}
}

class ResultButton extends InventoryButton{
	ResultButton(String label){
		super(label);
		this.setAlignment(Pos.CENTER);
		this.setPrefWidth(Screen.getPrimary().getBounds().getWidth() *.15);
		this.setId("ResultLabels");
	}
}

class PageLabel extends Label{
	public PageLabel(String label){
		this.setText(label);
		this.setAlignment(Pos.TOP_CENTER);
		this.setFont(Font.font("Arial",  FontWeight.EXTRA_BOLD, 
				(Screen.getPrimary().getBounds().getHeight() * Screen.getPrimary().getBounds().getWidth())*0.00005));
		this.setTextFill(Color.WHITE); 
	}
	
	public PageLabel(){
		this.setAlignment(Pos.TOP_CENTER);
		this.setFont(Font.font("Arial",  FontWeight.EXTRA_BOLD, 70));
		this.setTextFill(Color.WHITE);
	}
	
	
}

class InventoryRadioButton extends RadioButton{
	public InventoryRadioButton(String label){
		this.setFont(Font.font("Arial",  FontWeight.EXTRA_BOLD, 20));
		this.setTextFill(Color.WHITE);
	}
}

class AdditionPane extends GridPane{
	public AdditionPane(){
		this.setPadding(new Insets(20));;
		this.setAlignment(Pos.CENTER);

	
	}
}

class AdditionLabel extends PageLabel{
	public AdditionLabel(String label){
		this.setText(label);
		this.setAlignment(Pos.TOP_LEFT);
		this.setFont(Font.font("Arial",  FontWeight.BOLD, 25));
	}
}

class InventoryTextFields extends TextField{
	public InventoryTextFields(){
		this.getStyleClass().add("inputEntry");
		this.setFont(Font.font("Arial", 20));
		this.setAlignment(Pos.TOP_RIGHT);
		this.setLayoutX(this.getBoundsInLocal().getWidth());
	}
}





