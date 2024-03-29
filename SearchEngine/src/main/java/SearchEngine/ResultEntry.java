package SearchEngine;

public class ResultEntry {
    public String URL;
	public String title = "";
	public String positions;
	public String digest = "";
	public int position;
	public int numWordsTitle;
	
	public boolean finishJOB = false;
    
    

	public int numWordsTotal;   
	public int numWordsMatched;

    public String URLId;
    double titleTF;
    public String titleLoc;
    public double bodyTF;
    public String bodyLoc;

    public double pageRank = 0.000000001;
    public double score;
	
    
    public ResultEntry(String URLId, int numWords) {
    	this.URLId = URLId;
     
     
        numWordsTotal = numWords;
    }
    public void pageRank(double pageRank) {
    	this.pageRank = pageRank;
    }
    public int compareTo(ResultEntry other) {
        if (getScore() == other.getScore()) {
        
        
            return URLId.compareTo(other.URLId);
        } else {
            return getScore() < other.getScore() ? -1 : 1;
        }
    }



    public double getScore() {
        return score;
    }

}
