package com.anish.testengine.question.dto;

import java.util.ArrayList;

public class QuestionDTO {
	private int id;
	private String question;
	private ArrayList<String> options;
	private int answer;
	private int positiveScore;
	private int negativeScore;
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public ArrayList<String> getOptions() {
		return options;
	}
	public void setOptions(ArrayList<String> options) {
		this.options = options;
	}
	public int getAnswer() {
		return answer;
	}
	public void setAnswer(int answer) {
		this.answer = answer;
	}
	public int getPositiveScore() {
		return positiveScore;
	}
	public void setPositiveScore(int positiveScore) {
		this.positiveScore = positiveScore;
	}
	public int getNegativeScore() {
		return negativeScore;
	}
	public void setNegativeScore(int negativeScore) {
		this.negativeScore = negativeScore;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return "QuestionDTO [id=" + id + ", question=" + question + ", options=" + options + ", answer=" + answer
				+ ", positiveScore=" + positiveScore + ", negativeScore=" + negativeScore + "]";
	}
	
	
		
}
