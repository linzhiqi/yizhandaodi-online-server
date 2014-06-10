package game.logic;

import java.util.Arrays;
import java.util.List;

public class Question {
	
	private String question;
	private String answers;
	//checkMethod==0, character order matters
	//checkMethod==1, order doesn't matter
	private int checkType;

	public Question(String q, String a, int checkType) {
		this.question = q;
		this.answers = a;
		this.checkType = checkType;
	}

	public String getQuestion(){
		return this.question;
	}
	
	public boolean verifyAnswer(String answer){
		switch(checkType){
		case 0: {
			return ContainsCaseInsensitive(getAnswerList(this.answers),answer);
		}
		case 1: {
			return ContainsSameCharSetCaseInsensitive(getAnswerList(this.answers),answer);
		}
		default:{
			return ContainsCaseInsensitive(getAnswerList(this.answers),answer);
		}
		}
	}

	public String getStandardAnswer() {
		String[] list = answers.split("\\$");
		return list[0];
	}
	
	
	private List<String> getAnswerList(String answers) {
		String[] answerArray = answers.split("\\$");
		List<String> answerList = Arrays.asList(answerArray);
		return answerList;
	}
	

	private boolean ContainsCaseInsensitive(List<String> searchList, String searchTerm)
	{
	    for (String item : searchList)
	    {
	        if ((item.length()==searchTerm.length())&&item.equalsIgnoreCase(searchTerm)) {
	        	//System.out.println("玩家答案:" + searchTerm + "题库答案:" + item);
	        	return true;
	        }
	        //System.out.println("玩家答案:" + searchTerm + "题库答案:" + item); 
	    }
	    return false;
	}
	
	//verify without order
	private boolean ContainsSameCharSetCaseInsensitive(List<String> searchList, String searchTerm){
		for (String item : searchList)
	    {
	        if ((item.length()==searchTerm.length())&&SameCharSetCaseInsensitive(item, searchTerm)) 
	            return true;
	    }
	    return false;
	}
	
	private boolean SameCharSetCaseInsensitive(String a, String b){
		char[] arrayA=a.toLowerCase().toCharArray();
		char[] arrayB=b.toLowerCase().toCharArray();
		Arrays.sort(arrayA);
		Arrays.sort(arrayB);
		return Arrays.equals(arrayA, arrayB);
	}
}
