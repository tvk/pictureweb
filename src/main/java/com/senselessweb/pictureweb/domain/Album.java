package com.senselessweb.pictureweb.domain;

import java.util.Optional;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Album {
	
	private final JsonObject json;

	public Album(final JsonElement json) {
		this.json = Preconditions.checkNotNull(json).getAsJsonObject();
	}
	
	public String getId() {
		return json.get("id").getAsString();
	}

	public String getTitle() {
		return json.get("title").getAsString();
	}
	
	public Optional<String> getTitlePicture() {
		return Optional.ofNullable(this.json.get("primaryPhoto").getAsString());
	}

}
