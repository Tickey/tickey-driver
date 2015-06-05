package com.tickey.app.data.model;

import com.google.gson.annotations.SerializedName;

public class Post extends Feed {

	public static final String PARAM_KEY_ID = "postId";
	public static final String PARAM_KEY_TITLE = "message";
	public static final String KEY_IMAGE_URL = "imageUrl";
	public static final String KEY_FULL_MESSAGE = "fullMessage";
	public static final String KEY_IS_PUBLIC = "isPublic";

	@SerializedName("_id")
	public String id;

	@SerializedName("title")
	public String title;

	@SerializedName("shortMessage")
	public String shortMessage;

	@SerializedName("fullMessage")
	public String fullMessage;

	@SerializedName("imageUrl")
	public String postImageUrl;

	@SerializedName("createdAt")
	public String createdAt;

	@SerializedName("isLikedByUser")
	public boolean iLike;

	@SerializedName("likesCount")
	public int likes;

	@SerializedName("facebookShareInfo")
	public FacebookShareDetails fbShareDetails;
}
