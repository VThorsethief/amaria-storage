/*
 * Class for a compound 
 */


public class Compound{
	private String genericName;
	private String compoundID;
	private double ec90;
	double invasionNum;
	double dNASynthesisNum;
	double egressNum;
	double sexDiffNum;
	boolean invasionBinary;
	boolean dNASynthBin;
	boolean egressBin;
	boolean sexDiffBin;
	String source;
	String aquireInstructions;
	double purity;
	double ec50;
	double volume;
	double concentration;
	int boxNumber;
	int numberOfaAliquots;
	
	
	public Compound(){
		this.genericName = "Name not entered.";
		this.compoundID = "Fake Compound";
		this.ec90 = 0;
	}
	
	public Compound(String newGenericName,String newCompoundID){
		this.genericName = newGenericName;
		this.compoundID = newCompoundID;
		this.ec90 = 0;
	}
	
	public Compound(String newGenericName, String newCompoundID, double newEC90, double newEC50,
			double newPurity, String newSource, String newSourceInstructions, 
			double newConcentration, double newVolume, int numberOfAliquots){
		this.setCompoundID(newCompoundID);
		this.setGenericName(newGenericName);
		this.setEc90(newEC90);
		this.setEc50(newEC50);
		this.setPurity(newPurity);
		this.setSource(newSource);
		this.setAquireInstructions(newSourceInstructions);
		this.setConcentration(newConcentration);
		this.setVolume(newVolume);
		this.setNumberOfaAliquots(numberOfAliquots);
	}

	/**
	 * @return the genericName
	 */
	public String getGenericName() {
		return genericName;
	}

	/**
	 * @return the compoundID
	 */
	public String getCompoundID() {
		return compoundID;
	}

	/**
	 * @return the ec90
	 */
	public double getEc90() {
		return ec90;
	}

	/**
	 * @return the invasionNum
	 */
	public double getInvasionNum() {
		return invasionNum;
	}

	/**
	 * @return the dNASynthesisNum
	 */
	public double getdNASynthesisNum() {
		return dNASynthesisNum;
	}

	/**
	 * @return the egressNum
	 */
	public double getEgressNum() {
		return egressNum;
	}

	/**
	 * @return the sexDiffNum
	 */
	public double getSexDiffNum() {
		return sexDiffNum;
	}

	/**
	 * @return the invasionBinary
	 */
	public boolean isInvasionBinary() {
		return invasionBinary;
	}

	/**
	 * @return the dNASynthBin
	 */
	public boolean isdNASynthBin() {
		return dNASynthBin;
	}

	/**
	 * @return the egressBin
	 */
	public boolean isEgressBin() {
		return egressBin;
	}

	/**
	 * @return the sexDiffBin
	 */
	public boolean isSexDiffBin() {
		return sexDiffBin;
	}

	/**
	 * @param genericName the genericName to set
	 */
	public void setGenericName(String genericName) {
		if(genericName != ""){
			this.genericName = genericName;
		}else{
			this.genericName = "Generic name not entered";
		}
		
	}

	/**
	 * @param compoundID the compoundID to set
	 */
	public void setCompoundID(String compoundID) {
		if(compoundID != ""){
			this.compoundID = compoundID;
		}else{
			this.compoundID = "ID not entered";
		}
	}

	/**
	 * @param ec90 the ec90 to set
	 */
	public void setEc90(double ec90) {
		if(ec90 > 0){
			this.ec90 = ec90;
		}else{
			this.ec90 = 0;
		}
		
	}

	/**
	 * @param invasionNum the invasionNum to set
	 */
	public void setInvasionNum(double invasionNum) {
		this.invasionNum = invasionNum;
	}

	/**
	 * @param dNASynthesisNum the dNASynthesisNum to set
	 */
	public void setdNASynthesisNum(double dNASynthesisNum) {
		this.dNASynthesisNum = dNASynthesisNum;
	}

	/**
	 * @param egressNum the egressNum to set
	 */
	public void setEgressNum(double egressNum) {
		this.egressNum = egressNum;
	}

	/**
	 * @param sexDiffNum the sexDiffNum to set
	 */
	public void setSexDiffNum(double sexDiffNum) {
		this.sexDiffNum = sexDiffNum;
	}

	/**
	 * @param invasionBinary the invasionBinary to set
	 */
	public void setInvasionBinary(boolean invasionBinary) {
		this.invasionBinary = invasionBinary;
	}

	/**
	 * @param dNASynthBin the dNASynthBin to set
	 */
	public void setdNASynthBin(boolean dNASynthBin) {
		this.dNASynthBin = dNASynthBin;
	}

	/**
	 * @param egressBin the egressBin to set
	 */
	public void setEgressBin(boolean egressBin) {
		this.egressBin = egressBin;
	}

	/**
	 * @param sexDiffBin the sexDiffBin to set
	 */
	public void setSexDiffBin(boolean sexDiffBin) {
		this.sexDiffBin = sexDiffBin;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	/**
	 * @return the aquireInstructions
	 */
	public String getAquireInstructions() {
		return aquireInstructions;
	}

	/**
	 * @return the volume
	 */
	public double getVolume() {
		return volume;
	}

	/**
	 * @return the concentration
	 */
	public double getConcentration() {
		return concentration;
	}

	/**
	 * @return the boxNumber
	 */
	public int getBoxNumber() {
		return boxNumber;
	}

	/**
	 * @param boxNumber the boxNumber to set
	 */
	public void setBoxNumber(int boxNumber) {
		this.boxNumber = boxNumber;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(double volume) {
		if(volume > 0){
			this.volume = volume;
		}else{
			volume = 0;
		}
	}

	/**
	 * @param concentration the concentration to set
	 */
	public void setConcentration(double concentration) {
		if(concentration > 0){
			this.concentration = concentration;
		}else{
			this.concentration = 0;
		}
		
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		if(source != ""){
			this.source = source;
		}else{
			this.source = "Source not given";
		}
	}

	/**
	 * @param aquireInstructions the aquireInstructions to set
	 */
	public void setAquireInstructions(String aquireInstructions) {
		if(aquireInstructions != ""){
			this.aquireInstructions = aquireInstructions;
		}else{
			this.aquireInstructions = "Instructions not given";
		}
	}

	/**
	 * @return the purity
	 */
	public double getPurity() {
		return purity;
	}

	/**
	 * @return the ec50
	 */
	public double getEc50() {
		return ec50;
	}

	/**
	 * @param purity the purity to set
	 */
	public void setPurity(double purity) {
			this.purity = purity;
	}

	/**
	 * @param ec50 the ec50 to set
	 */
	public void setEc50(double ec50) {
		this.ec50 = ec50;
	}
	
	/**
	 * @return the numberOfaAliquots
	 */
	public int getNumberOfaAliquots() {
		return numberOfaAliquots;
	}

	/**
	 * @param numberOfaAliquots the numberOfaAliquots to set
	 */
	public void setNumberOfaAliquots(int numberOfaAliquots) {
		this.numberOfaAliquots = numberOfaAliquots;
	}
	
	public boolean removeAliquots(int amountRemoved){
		if(amountRemoved < this.numberOfaAliquots){
			this.numberOfaAliquots = this.numberOfaAliquots - amountRemoved;
			return true;
		}else{
			return false;
		}
	}

	@Override
	public String toString(){
		return this.compoundID;
	}
	
	@Override
	public boolean equals(Object o){
		 Compound comp = (Compound)o;
		if(this.getCompoundID() == comp.getCompoundID() & this.getVolume() == comp.getVolume() & 
				this.getConcentration() == comp.getConcentration()){
			return true;
		}else{
			return false;
		}
	}
	
	
}
