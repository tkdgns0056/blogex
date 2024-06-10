package com.blogex.api.repositrory;

import com.blogex.api.domain.Post;
import com.blogex.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}

