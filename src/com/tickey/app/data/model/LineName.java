package com.tickey.app.data.model;

import com.google.gson.annotations.SerializedName;

public class LineName extends Feed {

	public static final String KEY_IS_SUGGESTED = "is_suggested";
	public static final String KEY_NAME = "name";

	public SuggestState suggestionState = SuggestState.NOT_SUGGESTED;
	public GameState gameState = GameState.END;

	public static enum SuggestState {
		SUGGESTING, SUGGESTED, ERROR, NOT_SUGGESTED
	}

	public static enum GameState {
		SUGGEST("suggest"), VOTE("vote"), END("end");

		public static final String TAG = GameState.class.getSimpleName();

		private String name;

		private GameState(String value) {
			name = value;
		}
	}

	@SerializedName("_id")
	public String id;
	
	@SerializedName("name")
	public String name;

	@SerializedName("likesCount")
	public int votesCount;

	@SerializedName("canBeLiked")
	public boolean canBeLiked;
	
	public boolean isSelected = false;

	// createdAt: <date>

	public LineName() {
	}

	public LineName(String gameState) {
		for (GameState state : GameState.values()) {
			if (state.name.equalsIgnoreCase(gameState)) {
				this.gameState = state;
				break;
			}
		}
	}
}
