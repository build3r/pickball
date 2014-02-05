package com.example.pickball;

public class UserGameHistory {
	private String _gameText;
	private int _gameLuck;

	public UserGameHistory() {
		this._gameText = "";
		this._gameLuck = 0;
	}
	public void setGameText(String sGameText) {
		this._gameText = sGameText;
	}
	public String getGameText() {
		return _gameText;
	}
	public void setGameLuck(int iGameLuck) {
		this._gameLuck = iGameLuck;
	}
	public int getGameLuck() {
		return this._gameLuck;
	}	
}
